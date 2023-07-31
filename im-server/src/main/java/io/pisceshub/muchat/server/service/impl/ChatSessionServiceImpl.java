package io.pisceshub.muchat.server.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import io.pisceshub.muchat.common.core.enums.ChatType;
import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;
import io.pisceshub.muchat.server.common.entity.Group;
import io.pisceshub.muchat.server.common.entity.User;
import io.pisceshub.muchat.server.common.enums.GroupEnum;
import io.pisceshub.muchat.server.common.enums.UserEnum;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionAddReq;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionInfoResp;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionUpdateReq;
import io.pisceshub.muchat.server.common.vo.user.UserVO;
import io.pisceshub.muchat.server.exception.BusinessException;
import io.pisceshub.muchat.server.exception.NotJoinGroupException;
import io.pisceshub.muchat.server.service.*;
import io.pisceshub.muchat.server.service.business.chatsession.ChatSessionSave;
import io.pisceshub.muchat.server.util.BeanUtils;
import io.pisceshub.muchat.server.util.MessageUtils;
import io.pisceshub.muchat.server.util.SessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:45
 */
@Service
public class ChatSessionServiceImpl implements IChatSessionService {

  @Autowired
  private ChatSessionSave chatSessionSave;

  @Autowired
  private IUserService iUserService;

  @Autowired
  private IGroupService iGroupService;

  @Autowired
  private IGroupMessageService iGroupMessageService;

  @Autowired
  private IPrivateMessageService iPrivateMessageService;

  @Autowired
  private IGroupMemberService iGroupMemberService;

  @Override
  public boolean save(Long userId, ChatSessionAddReq vo) {

    // todo 校验对于id和类型的合法性
    ChatSessionInfoDto dto = new ChatSessionInfoDto();
    BeanUtils.copyProperties(vo, dto);
    return chatSessionSave.add(userId, dto);
  }

  private Result<Set<ChatSessionInfoResp>> annoySessionList(Long userId) {
    List<Group> groupList = iGroupService.findByGroupType(GroupEnum.GroupType.Anonymous.getCode());
    Set<ChatSessionInfoResp> list = groupList.stream().map(e -> {
      ChatSessionInfoResp chatSessionInfoResp = new ChatSessionInfoResp();
      chatSessionInfoResp.setChatType(ChatType.GROUP);
      chatSessionInfoResp.setTargetId(e.getId());
      chatSessionInfoResp.setName(e.getName());
      chatSessionInfoResp.setHeadImage(e.getHeadImage());
      chatSessionInfoResp.setUnReadCount(0L);
      chatSessionInfoResp.setMessages(Collections.emptyList());
      chatSessionInfoResp.setLastSendTime(e.getCreatedTime().getTime());
      return chatSessionInfoResp;
    }).collect(Collectors.toSet());
    if (CollUtil.isEmpty(list)) {
      return ResultUtils.success(Collections.emptySet());
    }

    User user = null;
    for (ChatSessionInfoResp resp : list) {
      if (!iGroupMemberService.memberExsit(userId, resp.getTargetId())) {
        // 加入群聊
        if (user == null) {
          user = iUserService.getById(userId);
          if (user == null) {
            throw new BusinessException(ResultCode.INVALID_TOKEN);
          }
        }
        iGroupMemberService.joinGroup(resp.getTargetId(), resp.getName(), user);
      } else {
        // 已经加入过，查询历史消息
        List<GroupMessageInfo> historyMessage = iGroupMessageService.findHistoryMessage(
            resp.getTargetId(),
            null);
        if (CollUtil.isNotEmpty(historyMessage)) {
          resp.setLastSendTime(historyMessage.get(0).getSendTime().getTime());
          resp.setLastContent(historyMessage.get(0).getContent());
          resp.setMessages(historyMessage);
        }
      }

    }
    return ResultUtils.success(list);
  }

  @Override
  public Result<Set<ChatSessionInfoResp>> list() {
    SessionContext.UserSessionInfo session = SessionContext.getSession();
    Set<ChatSessionInfoDto> list = null;
    if (UserEnum.AccountType.Anonymous.getCode().equals(session.getAccountType())) {
      return this.annoySessionList(session.getId());
    } else {
      list = chatSessionSave.list(SessionContext.getUserId());
    }
    if (CollUtil.isEmpty(list)) {
      return ResultUtils.success(Collections.emptySet());
    }
    Set<ChatSessionInfoResp> result = new HashSet<>(list.size());
    Long userId = SessionContext.getUserId();
    for (ChatSessionInfoDto dto : list) {
      ChatType chatType = dto.getChatType();
      Long targetId = dto.getTargetId();
      Long lastSendTime = dto.getCreateTime();
      String lastContent = null;
      List message = Collections.emptyList();
      String name = null;
      String headImage = null;
      switch (chatType) {
        case GROUP:
          try {
            // 查询群信息
            Group group = iGroupService.findBaseInfoById(targetId);
            if (group == null) {
              continue;
            }
            name = group.getName();
            headImage = group.getHeadImage();
            // 查询消息
            List<GroupMessageInfo> historyMessage = iGroupMessageService.findHistoryMessage(
                targetId, null);
            if (CollUtil.isNotEmpty(historyMessage)) {
              GroupMessageInfo messageInfo = historyMessage.get(0);
              lastSendTime = messageInfo.getSendTime().getTime();
              lastContent = MessageUtils.converMessageContent(messageInfo.getType(),
                  messageInfo.getContent());
              message = historyMessage;
            }
          } catch (NotJoinGroupException e) {
            continue;
          }
          break;
        case PRIVATE:
          UserVO userVO = iUserService.findByUserIdAndFriendId(targetId, userId);
          if (userVO == null) {
            continue;
          }
          name = userVO.getNickName();
          headImage = userVO.getHeadImage();
          // 查询消息
          List<PrivateMessageInfo> historyMessage = iPrivateMessageService.findHistoryMessage(
              targetId, null);
          if (CollUtil.isNotEmpty(historyMessage)) {
            PrivateMessageInfo messageInfo = historyMessage.get(0);
            lastSendTime = messageInfo.getSendTime().getTime();
            lastContent = MessageUtils.converMessageContent(messageInfo.getType(),
                messageInfo.getContent());
            message = historyMessage;
          }
      }
      ChatSessionInfoResp.ChatSessionInfoRespBuilder builder = ChatSessionInfoResp.builder()
          .chatType(chatType)
          .messages(message)
          .targetId(targetId)
          .unReadCount(0L)
          .name(name)
          .headImage(headImage)
          .lastContent(lastContent)
          .lastSendTime(lastSendTime);
      result.add(builder.build());
    }

    return ResultUtils.success(result);
  }

  @Override
  public boolean del(ChatSessionUpdateReq vo) {
    Long userId = ObjectUtil.defaultIfNull(vo.getUserId(), SessionContext.getUserId());
    return chatSessionSave.del(userId, BeanUtils.copyProperties(vo, ChatSessionInfoDto.class));
  }

}
