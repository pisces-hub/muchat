package io.pisceshub.muchat.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.pisceshub.muchat.common.core.enums.ChatType;
import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.server.adapter.IpSearchAdapter;
import io.pisceshub.muchat.server.common.contant.Constant;
import io.pisceshub.muchat.server.common.contant.RedisKey;
import io.pisceshub.muchat.server.common.entity.Friend;
import io.pisceshub.muchat.server.common.entity.Group;
import io.pisceshub.muchat.server.common.entity.GroupMember;
import io.pisceshub.muchat.server.common.entity.User;
import io.pisceshub.muchat.server.common.enums.GroupEnum;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionUpdateReq;
import io.pisceshub.muchat.server.common.vo.user.GroupInviteReq;
import io.pisceshub.muchat.server.common.vo.user.GroupMemberResp;
import io.pisceshub.muchat.server.common.vo.user.GroupVO;
import io.pisceshub.muchat.server.exception.BusinessException;
import io.pisceshub.muchat.server.exception.GlobalException;
import io.pisceshub.muchat.server.exception.NotJoinGroupException;
import io.pisceshub.muchat.server.mapper.GroupMapper;
import io.pisceshub.muchat.server.service.*;
import io.pisceshub.muchat.server.util.BeanUtils;
import io.pisceshub.muchat.server.util.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@CacheConfig(cacheNames = RedisKey.IM_CACHE_GROUP)
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements IGroupService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IGroupMemberService groupMemberService;

    @Autowired
    private IFriendService friendsService;

    @Autowired
    private IpSearchAdapter ipSearchAdapter;

    @Autowired
    private IChatSessionService iChatSessionService;


    /**
     * 创建新群聊
     *
     * @return
     * @Param groupName 群聊名称
     **/
    @Transactional
    @Override
    public GroupVO createGroup(String groupName) {
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        User user = userService.getById(session.getId());
        // 保存群组数据
        Group group = new Group();
        group.setName(groupName);
        group.setOwnerId(user.getId());
        group.setHeadImage(user.getHeadImage());
        group.setHeadImageThumb(user.getHeadImageThumb());
        this.save(group);
        // 把群主加入群
        GroupMember groupMember = new GroupMember();
        groupMember.setGroupId(group.getId());
        groupMember.setUserId(user.getId());
        groupMember.setAliasName(user.getNickName());
        groupMember.setRemark(groupName);
        groupMember.setHeadImage(user.getHeadImageThumb());
        groupMemberService.save(groupMember);
        GroupVO vo = BeanUtils.copyProperties(group, GroupVO.class);
        vo.setAliasName(user.getNickName());
        vo.setRemark(groupName);
        log.info("创建群聊，群聊id:{},群聊名称:{}", group.getId(), group.getName());
        return vo;
    }


    /**
     * 修改群聊信息
     *
     * @return
     * @Param GroupVO 群聊信息
     **/
    @Transactional
    @Override
    public GroupVO modifyGroup(GroupVO vo) {
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        // 校验是不是群主，只有群主能改信息
        Group group = this.getById(vo.getId());
        // 群主有权修改群基本信息
        if (group.getOwnerId() == session.getId()) {
            group = BeanUtils.copyProperties(vo, Group.class);
            this.updateById(group);
        }
        // 更新成员信息
        GroupMember member = groupMemberService.findByGroupAndUserId(vo.getId(), session.getId());
        if (member == null) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "您不是群聊的成员");
        }
        member.setAliasName(StringUtils.isEmpty(vo.getAliasName()) ? session.getNickName() : vo.getAliasName());
        member.setRemark(StringUtils.isEmpty(vo.getRemark()) ? group.getName() : vo.getRemark());
        groupMemberService.updateById(member);
        log.info("修改群聊，群聊id:{},群聊名称:{}", group.getId(), group.getName());
        return vo;
    }


    /**
     * 删除群聊
     *
     * @return
     * @Param groupId 群聊id
     **/
    @Transactional
    @Override
    public void deleteGroup(Long groupId) {
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        Group group = this.getById(groupId);
        if (group.getOwnerId() != session.getId()) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "只有群主才有权限解除群聊");
        }
        // 逻辑删除群数据
        group.setDeleted(true);
        this.updateById(group);
        // 删除成员数据
        groupMemberService.removeByGroupId(groupId);
        log.info("删除群聊，群聊id:{},群聊名称:{}", group.getId(), group.getName());
    }


    /**
     * 退出群聊
     *
     * @param groupId 群聊id
     * @return
     */
    @Override
    public void quitGroup(Long groupId) {
        Long userId = SessionContext.getSession().getId();
        Group group = this.getById(groupId);
        if (group.getOwnerId() == userId) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "您是群主，不可退出群聊");
        }
        // 删除群聊成员
        groupMemberService.removeByGroupAndUserId(groupId, userId);

        iChatSessionService.del(ChatSessionUpdateReq.builder()
                .chatType(ChatType.GROUP)
                .targetId(groupId).build()
        );
        log.info("退出群聊，群聊id:{},群聊名称:{},用户id:{}", group.getId(), group.getName(), userId);
    }


    /**
     * 将用户踢出群聊
     *
     * @param groupId 群聊id
     * @param userId  用户id
     * @return
     */
    @Override
    public void kickGroup(Long groupId, Long userId) {
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        Group group = this.getById(groupId);
        if (group.getOwnerId() != session.getId()) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "您不是群主，没有权限踢人");
        }
        if (userId == session.getId()) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "亲，不能自己踢自己哟");
        }
        // 删除群聊成员
        groupMemberService.removeByGroupAndUserId(groupId, userId);
        iChatSessionService.del(ChatSessionUpdateReq.builder()
                .chatType(ChatType.GROUP)
                        .userId(userId)
                .targetId(groupId).build());
        log.info("踢出群聊，群聊id:{},群聊名称:{},用户id:{}", group.getId(), group.getName(), userId);
    }

    @Override
    public Group findBaseInfoById(Long groupId){
        LambdaQueryWrapper<Group> groupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        groupLambdaQueryWrapper.eq(Group::getDeleted,false);
        groupLambdaQueryWrapper.eq(Group::getId,groupId);
        return this.getOne(groupLambdaQueryWrapper);
    }


    @Override
    public GroupVO findById(Long groupId) {
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        Group group = this.findBaseInfoById(groupId);
        if(group==null){
            throw new BusinessException("群聊信息不存在");
        }
        GroupMember member = groupMemberService.findByGroupAndUserId(groupId, session.getId());
        if (member == null) {
            throw new NotJoinGroupException(ResultCode.PROGRAM_ERROR, "您未加入群聊");
        }
        GroupVO vo = BeanUtils.copyProperties(group, GroupVO.class);
        if(GroupEnum.GroupType.Anonymous.getCode().equals(group.getGroupType())){
            vo.setAliasName(member.getAliasName());
            vo.setRemark(group.getName());
        }else{
            vo.setAliasName(member.getAliasName());
            vo.setRemark(member.getRemark());
        }
        vo.setMemberCount(groupMemberService.findMemberCount(groupId));
        return vo;
    }


    /**
     * 查询当前用户的所有群聊
     *
     * @return
     **/
    @Override
    public List<GroupVO> findGroups() {
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        // 查询当前用户的群id列表
        List<GroupMember> groupMembers = groupMemberService.findByUserId(session.getId());
        if (groupMembers.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        // 拉取群列表
        List<Long> ids = groupMembers.stream().map((gm -> gm.getGroupId())).collect(Collectors.toList());
        QueryWrapper<Group> groupWrapper = new QueryWrapper();
        groupWrapper.lambda().in(Group::getId, ids);
        List<Group> groups = this.list(groupWrapper);
        // 转vo
        List<GroupVO> vos = groups.stream().map(g -> {
            GroupVO vo = BeanUtils.copyProperties(g, GroupVO.class);
            GroupMember member = groupMembers.stream().filter(m -> g.getId().equals(m.getGroupId())).findFirst().get();
            vo.setAliasName(member.getAliasName());
            vo.setRemark(member.getRemark());
            return vo;
        }).collect(Collectors.toList());
        return vos;
    }

    /**
     * 邀请好友进群
     *
     * @return
     * @Param GroupInviteVO  群id、好友id列表
     **/
    @Override
    public void invite(GroupInviteReq vo) {
        Group group = this.getById(vo.getGroupId());
        if (group == null) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "群聊不存在");
        }
        if (GroupEnum.GroupType.Anonymous.getCode().equals(group.getGroupType())) {
            throw new BusinessException("不允许加入");
        }
        // 群聊人数校验
        List<GroupMember> members = groupMemberService.findByGroupId(vo.getGroupId());
        long size = members.stream().filter(m -> !m.getQuit()).count();
        if (vo.getFriendIds().size() + size > Constant.MAX_GROUP_MEMBER) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "群聊人数不能大于" + Constant.MAX_GROUP_MEMBER + "人");
        }

        List<GroupMember> groupMembers = new ArrayList<>();
        // 找出好友信息
        if (vo.getUserId() != null) {
            List<Friend> friends = friendsService.findFriendByUserId(vo.getUserId());
            List<Friend> friendsList = vo.getFriendIds().stream().map(id ->
                    friends.stream().filter(f -> f.getFriendId().equals(id)).findFirst().get()).collect(Collectors.toList());
            if (friendsList.size() != vo.getFriendIds().size()) {
                throw new GlobalException(ResultCode.PROGRAM_ERROR, "部分用户不是您的好友，邀请失败");
            }
            groupMembers = friendsList.stream()
                    .map(f -> {
                        Optional<GroupMember> optional = members.stream().filter(m -> m.getUserId() == f.getFriendId()).findFirst();
                        GroupMember groupMember = optional.isPresent() ? optional.get() : new GroupMember();
                        groupMember.setGroupId(vo.getGroupId());
                        groupMember.setUserId(f.getFriendId());
                        groupMember.setAliasName(f.getFriendNickName());
                        groupMember.setRemark(group.getName());
                        groupMember.setHeadImage(f.getFriendHeadImage());
                        groupMember.setCreatedTime(new Date());
                        groupMember.setQuit(false);
                        return groupMember;
                    }).collect(Collectors.toList());
        } else {
            List<User> users = userService.listByIds(vo.getFriendIds());
            List<Long> memberIds = members.stream().map(GroupMember::getUserId).collect(Collectors.toList());
            groupMembers = users.stream().filter(e -> !memberIds.contains(e.getId())).map(f -> {
                GroupMember groupMember = new GroupMember();
                groupMember.setGroupId(vo.getGroupId());
                groupMember.setUserId(f.getId());
                groupMember.setAliasName(f.getNickName());
                groupMember.setRemark(group.getName());
                groupMember.setHeadImage(f.getHeadImage());
                groupMember.setCreatedTime(new Date());
                groupMember.setQuit(false);
                return groupMember;
            }).collect(Collectors.toList());
        }

        // 批量保存成员数据
        if (!groupMembers.isEmpty()) {
            groupMemberService.saveOrUpdateBatch(group.getId(), groupMembers);
        }
        log.info("邀请进入群聊，群聊id:{},群聊名称:{},被邀请用户id:{}", group.getId(), group.getName(), vo.getFriendIds());
    }

    /**
     * 查询群成员
     *
     * @return List<GroupMemberVO>
     * @Param groupId 群聊id
     **/
    @Override
    public List<GroupMemberResp> findGroupMembers(Long groupId) {
        GroupVO groupVO = findById(groupId);
        if(groupVO==null){
            throw new GlobalException("群聊不存在");
        }
        List<GroupMember> members = groupMemberService.findByGroupId(groupId);
        Set<Long> memberIds = members.stream()
                .map(e -> e.getUserId()).collect(Collectors.toSet());
        if (CollUtil.isEmpty(members)) {
            return Collections.emptyList();
        }
        List<User> users = userService.listByIds(memberIds);
        if (CollUtil.isEmpty(users)) {
            return Collections.emptyList();
        }
        Map<Long, User> ipMap = users.stream()
                .collect(Collectors.toMap(User::getId,
                        e -> e));
        users.clear();
        return members.stream()
                .filter(e -> ipMap.containsKey(e.getUserId()))
                .map(m -> {
                    GroupMemberResp vo = BeanUtils.copyProperties(m, GroupMemberResp.class);
                    User user = ipMap.get(vo.getUserId());
                    vo.setIpAddress(ipSearchAdapter.search(user.getLastLoginIp()));
                    if(GroupEnum.GroupType.Anonymous.getCode().equals(groupVO.getGroupType())){
                        vo.setAliasName(user.getNickName());
                        vo.setHeadImage(user.getHeadImage());
                        vo.setRemark(groupVO.getName());
                    }
                    vo.setOnlineState(userService.isOnline(m.getUserId()));
                    if(m.getUserId().equals(SessionContext.getUserId())){
                        vo.setOnlineState(true);
                    }
                    return vo;
                }).sorted(new Comparator<GroupMemberResp>() {
                    @Override
                    public int compare(GroupMemberResp o1, GroupMemberResp o2) {
                        //根据在线状态排序
                        if(o1.getOnlineState() && o2.getOnlineState()){
                            return 1;
                        }else if(o1.getOnlineState()){
                            return -1;
                        }else if(o2.getOnlineState()){
                            return 1;
                        }else {
                            return -1;
                        }
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public List<Group> findByGroupType(Integer code) {
        return lambdaQuery().eq(Group::getGroupType, code)
                .eq(Group::getDeleted,false)
        .orderByAsc(Group::getCreatedTime).list();
    }




}
