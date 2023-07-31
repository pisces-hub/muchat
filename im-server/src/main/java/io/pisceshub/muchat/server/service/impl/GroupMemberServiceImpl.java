package io.pisceshub.muchat.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.pisceshub.muchat.common.core.model.PageResp;
import io.pisceshub.muchat.server.adapter.IpSearchAdapter;
import io.pisceshub.muchat.server.common.contant.RedisKey;
import io.pisceshub.muchat.server.common.entity.GroupMember;
import io.pisceshub.muchat.server.common.entity.User;
import io.pisceshub.muchat.server.common.vo.group.GroupMemberQueryReq;
import io.pisceshub.muchat.server.common.vo.user.GroupMemberResp;
import io.pisceshub.muchat.server.mapper.GroupMemberMapper;
import io.pisceshub.muchat.server.service.IGroupMemberService;
import io.pisceshub.muchat.server.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CacheConfig(cacheNames = RedisKey.IM_CACHE_GROUP_MEMBER_ID)
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements
    IGroupMemberService {

  @Autowired
  private IUserService iUserService;

  @Autowired
  private IpSearchAdapter ipSearchAdapter;

  /**
   * 添加群聊成员
   *
   * @param member 成员
   * @return
   */
  @Override
  public boolean save(GroupMember member) {
    return super.save(member);
  }

  /**
   * 批量添加成员
   *
   * @param groupId 群聊id
   * @param members 成员列表
   * @return
   */
  @Override
  public boolean saveOrUpdateBatch(Long groupId, List<GroupMember> members) {
    return super.saveOrUpdateBatch(members);
  }

  /**
   * 根据群聊id和用户id查询群聊成员
   *
   * @param groupId 群聊id
   * @param userId  用户id
   * @return
   */
  @Override
  public GroupMember findByGroupAndUserId(Long groupId, Long userId) {
    QueryWrapper<GroupMember> wrapper = new QueryWrapper<>();
    wrapper.lambda().eq(GroupMember::getGroupId, groupId).eq(GroupMember::getUserId, userId);
    return this.getOne(wrapper);
  }

  /**
   * 根据用户id查询群聊成员
   *
   * @param userId 用户id
   * @return
   */
  @Override
  public List<GroupMember> findByUserId(Long userId) {
    QueryWrapper<GroupMember> memberWrapper = new QueryWrapper();
    memberWrapper.lambda().eq(GroupMember::getUserId, userId).eq(GroupMember::getQuit, false);
    return this.list(memberWrapper);
  }

  /**
   * 根据群聊id查询群聊成员（包括已退出）
   *
   * @param groupId 群聊id
   * @return
   */
  @Override
  public List<GroupMember> findByGroupId(Long groupId) {
    QueryWrapper<GroupMember> memberWrapper = new QueryWrapper();
    memberWrapper.lambda().eq(GroupMember::getGroupId, groupId);
    return this.list(memberWrapper);
  }

  /**
   * 根据群聊id查询没有退出的群聊成员id
   *
   * @param groupId 群聊id
   * @return
   */
  @Override
  public List<Long> findUserIdsByGroupId(Long groupId) {
    QueryWrapper<GroupMember> memberWrapper = new QueryWrapper();
    memberWrapper.lambda().eq(GroupMember::getGroupId, groupId).eq(GroupMember::getQuit, false);
    List<GroupMember> members = this.list(memberWrapper);
    return members.stream().map(m -> m.getUserId()).collect(Collectors.toList());
  }

  /**
   * 根据群聊id删除移除成员
   *
   * @param groupId 群聊id
   * @return
   */
  @Override
  public void removeByGroupId(Long groupId) {
    UpdateWrapper<GroupMember> wrapper = new UpdateWrapper();
    wrapper.lambda().eq(GroupMember::getGroupId, groupId).set(GroupMember::getQuit, true);
    this.update(wrapper);
  }

  /**
   * 根据群聊id和用户id移除成员
   *
   * @param groupId 群聊id
   * @param userId  用户id
   * @return
   */
  @Override
  public void removeByGroupAndUserId(Long groupId, Long userId) {
    UpdateWrapper<GroupMember> wrapper = new UpdateWrapper<>();
    wrapper.lambda()
        .eq(GroupMember::getGroupId, groupId)
        .eq(GroupMember::getUserId, userId)
        .set(GroupMember::getQuit, true);
    this.update(wrapper);
  }

  @Override
  public boolean joinGroup(Long groupId, String remark, User user) {
    GroupMember groupMember = new GroupMember();
    groupMember.setGroupId(groupId);
    groupMember.setUserId(user.getId());
    groupMember.setAliasName(user.getNickName());
    groupMember.setRemark(remark);
    groupMember.setHeadImage(user.getHeadImage());
    groupMember.setCreatedTime(new Date());
    groupMember.setQuit(false);
    // 查询是否已经加入过
    Integer count = lambdaQuery().eq(GroupMember::getGroupId, groupId)
        .eq(GroupMember::getUserId, groupMember.getUserId())
        .count();
    if (count > 0) {
      return true;
    }

    return this.save(groupMember);
  }

  @Override
  public boolean memberExsit(Long userId, Long groupId) {
    return lambdaQuery().eq(GroupMember::getGroupId, groupId).eq(GroupMember::getUserId, userId)
        .count() > 0;
  }

  @Override
  public PageResp<GroupMemberResp> findGroupMembersV2(GroupMemberQueryReq req) {
    Page<GroupMember> groupMemberPage = this.baseMapper
        .findGroupMembersV2(new Page<>(req.getPageNo(), req.getPageSize()), req.getGroupId(),
            req.getSearch());
    List<GroupMember> records = groupMemberPage.getRecords();
    if (CollUtil.isEmpty(records)) {
      return PageResp.empty();
    }
    Set<Long> memberIds = records.stream().map(e -> e.getUserId()).collect(Collectors.toSet());
    List<User> users = iUserService.listByIds(memberIds);
    if (CollUtil.isEmpty(users)) {
      return PageResp.empty();
    }
    Map<Long, User> ipMap = users.stream().collect(Collectors.toMap(User::getId, e -> e));
    List<GroupMemberResp> memberList = records.stream().map(e -> {
      GroupMemberResp groupMemberResp = BeanUtil.copyProperties(e, GroupMemberResp.class);
      groupMemberResp.setOnlineState(iUserService.isOnline(e.getUserId()));
      User user = ipMap.get(e.getUserId());
      groupMemberResp.setIpAddress(ipSearchAdapter.search(user.getLastLoginIp()));
      return groupMemberResp;
    }).collect(Collectors.toList());

    return PageResp.toPage(memberList, groupMemberPage.hasNext());
  }

  @Override
  public Integer findMemberCount(Long groupId) {
    return lambdaQuery().eq(GroupMember::getGroupId, groupId).count();
  }
}
