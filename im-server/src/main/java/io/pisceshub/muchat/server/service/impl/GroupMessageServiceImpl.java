package io.pisceshub.muchat.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.enums.MessageStatus;
import io.pisceshub.muchat.common.core.enums.MessageType;
import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.sdk.IMClient;
import io.pisceshub.muchat.server.adapter.SensitiveWordAdapter;
import io.pisceshub.muchat.server.common.contant.RedisKey;
import io.pisceshub.muchat.server.common.entity.Group;
import io.pisceshub.muchat.server.common.entity.GroupMember;
import io.pisceshub.muchat.server.common.entity.GroupMessage;
import io.pisceshub.muchat.server.common.enums.GroupEnum;
import io.pisceshub.muchat.server.common.vo.message.GroupMessageSendReq;
import io.pisceshub.muchat.server.common.vo.message.MessageSendResp;
import io.pisceshub.muchat.server.common.vo.user.GroupVO;
import io.pisceshub.muchat.server.exception.GlobalException;
import io.pisceshub.muchat.server.mapper.GroupMessageMapper;
import io.pisceshub.muchat.server.service.IGroupMemberService;
import io.pisceshub.muchat.server.service.IGroupMessageService;
import io.pisceshub.muchat.server.service.IGroupService;
import io.pisceshub.muchat.server.util.BeanUtils;
import io.pisceshub.muchat.server.util.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GroupMessageServiceImpl extends ServiceImpl<GroupMessageMapper, GroupMessage> implements IGroupMessageService {

    @Autowired
    private IGroupService                 groupService;

    @Autowired
    private IGroupMemberService           groupMemberService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private IMClient                      imClient;

    @Autowired
    private GroupMessageMapper            groupMessageMapper;

    /**
     * 默认一次查询多少条消息
     */
    private final static Integer          defaultQueryMessageCount = 15;

    @Autowired
    private SensitiveWordAdapter          sensitiveWordAdapter;

    /**
     * 发送群聊消息(与mysql所有交换都要进行缓存)
     *
     * @param vo
     * @return 群聊id
     */
    @Override
    public MessageSendResp sendMessage(GroupMessageSendReq vo) {
        Long userId = SessionContext.getSession().getId();
        Group group = groupService.getById(vo.getGroupId());
        if (group == null) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "群聊不存在");
        }
        if (group.getDeleted()) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "群聊已解散");
        }
        // 判断是否在群里
        List<Long> userIds = groupMemberService.findUserIdsByGroupId(group.getId());
        if (!userIds.contains(userId)) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "您已不在群聊里面，无法发送消息");
        }

        String replaced = sensitiveWordAdapter.replace(vo.getContent());
        if (replaced.matches("^\\*+$")) {
            throw new GlobalException("不允许发送该消息内容");
        }
        vo.setContent(replaced);

        // 保存消息
        GroupMessage msg = BeanUtils.copyProperties(vo, GroupMessage.class);
        msg.setSendId(userId);
        msg.setSendTime(new Date());
        this.save(msg);
        // 不用发给自己
        userIds = userIds.stream().filter(id -> userId != id).collect(Collectors.toList());
        // 群发
        GroupMessageInfo msgInfo = BeanUtils.copyProperties(msg, GroupMessageInfo.class);
        imClient.sendGroupMessage(userIds, msgInfo);
        log.info("发送群聊消息，发送id:{},群聊id:{},内容:{}", userId, vo.getGroupId(), vo.getContent());

        return MessageSendResp.builder().id(msg.getId()).content(msg.getContent()).build();
    }

    /**
     * 撤回消息
     *
     * @param id 消息id
     */
    @Override
    public void recallMessage(Long id) {
        Long userId = SessionContext.getSession().getId();
        GroupMessage msg = this.getById(id);
        if (msg == null) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "消息不存在");
        }
        if (msg.getSendId() != userId) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "这条消息不是由您发送,无法撤回");
        }
        if (System.currentTimeMillis() - msg.getSendTime().getTime() > AppConst.ALLOW_RECALL_SECOND * 1000) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "消息已发送超过5分钟，无法撤回");
        }
        // 判断是否在群里
        GroupMember member = groupMemberService.findByGroupAndUserId(msg.getGroupId(), userId);
        if (member == null) {
            throw new GlobalException(ResultCode.PROGRAM_ERROR, "您已不在群聊里面，无法撤回消息");
        }
        // 修改数据库
        msg.setStatus(MessageStatus.RECALL.code());
        this.updateById(msg);
        // 群发
        List<Long> userIds = groupMemberService.findUserIdsByGroupId(msg.getGroupId());
        // 不用发给自己
        userIds = userIds.stream().filter(uid -> userId.equals(uid)).collect(Collectors.toList());
        GroupMessageInfo msgInfo = BeanUtils.copyProperties(msg, GroupMessageInfo.class);
        msgInfo.setType(MessageType.TIP.code());
        String content = String.format("'%s'撤回了一条消息", member.getAliasName());
        msgInfo.setContent(content);
        msgInfo.setSendTime(new Date());
        imClient.sendGroupMessage(userIds, msgInfo);
        log.info("撤回群聊消息，发送id:{},群聊id:{},内容:{}", userId, msg.getGroupId(), msg.getContent());
    }

    /**
     * 异步拉取群聊消息，通过websocket异步推送
     *
     * @return
     */
    @Override
    public void pullUnreadMessage() {
        Long userId = SessionContext.getSession().getId();
        List<Long> recvIds = new LinkedList();
        recvIds.add(userId);
        List<GroupMember> members = groupMemberService.findByUserId(userId);
        for (GroupMember member : members) {
            // 获取群聊已读的最大消息id，只推送未读消息
            String key = RedisKey.IM_GROUP_READED_POSITION + member.getGroupId() + ":" + userId;
            Integer maxReadedId = (Integer) redisTemplate.opsForValue().get(key);
            QueryWrapper<GroupMessage> wrapper = new QueryWrapper();
            wrapper.lambda()
                .eq(GroupMessage::getGroupId, member.getGroupId())
                .gt(GroupMessage::getSendTime, member.getCreatedTime())
                .ne(GroupMessage::getSendId, userId)
                .ne(GroupMessage::getStatus, MessageStatus.RECALL.code());
            if (maxReadedId != null) {
                wrapper.lambda().gt(GroupMessage::getId, maxReadedId);
            }
            wrapper.last("limit 100");
            List<GroupMessage> messages = this.list(wrapper);
            if (messages.isEmpty()) {
                continue;
            }
            // 组装消息，准备推送
            List<GroupMessageInfo> messageInfos = messages.stream().map(m -> {
                GroupMessageInfo msgInfo = BeanUtils.copyProperties(m, GroupMessageInfo.class);
                return msgInfo;
            }).collect(Collectors.toList());
            // 发送消息
            GroupMessageInfo[] infoArr = messageInfos.toArray(new GroupMessageInfo[messageInfos.size()]);
            imClient.sendGroupMessage(Collections.singletonList(userId), infoArr);
            log.info("拉取未读群聊消息，用户id:{},群聊id:{},数量:{}", userId, member.getGroupId(), messageInfos.size());
        }

    }

    /**
     * 拉取历史聊天记录
     *
     * @param groupId 群聊id
     * @return 聊天记录列表
     */
    @Override
    public List<GroupMessageInfo> findHistoryMessage(Long groupId, Long lastMessageId) {
        GroupVO groupVO = groupService.findById(groupId);
        if (groupVO == null) {
            throw new GlobalException("群聊不存在");
        }
        Date beforeDate = null;
        if (GroupEnum.GroupType.Plain.getCode().equals(groupVO.getGroupType())) {
            Long userId = SessionContext.getSession().getId();
            // 群聊成员信息
            GroupMember member = groupMemberService.findByGroupAndUserId(groupId, userId);
            if (member == null || member.getQuit()) {
                throw new GlobalException(ResultCode.PROGRAM_ERROR, "您已不在群聊中");
            }
            // 查询聊天记录，只查询加入群聊时间之后的消息
            beforeDate = member.getCreatedTime();
        }
        List<GroupMessage> messages = groupMessageMapper
            .findHistoryMessage(groupId, beforeDate, lastMessageId, defaultQueryMessageCount);
        List<GroupMessageInfo> messageInfos = messages.stream().map(m -> {
            GroupMessageInfo groupMessageInfo = BeanUtil.copyProperties(m, GroupMessageInfo.class);
            return groupMessageInfo;
        }).collect(Collectors.toList());
        return messageInfos;
    }

}
