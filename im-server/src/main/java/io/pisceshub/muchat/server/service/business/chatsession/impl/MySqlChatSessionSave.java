package io.pisceshub.muchat.server.service.business.chatsession.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.pisceshub.muchat.common.core.enums.ChatType;
import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;
import io.pisceshub.muchat.server.common.entity.ChatSession;
import io.pisceshub.muchat.server.mapper.ChatSessionMapper;
import io.pisceshub.muchat.server.service.business.chatsession.ChatSessionSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
@Component
public class MySqlChatSessionSave implements ChatSessionSave {

  private final static Long MAX_SIZE = 100L;

  @Autowired
  private ChatSessionMapper chatSessionMapper;

  @Override
  public boolean add(Long userId, ChatSessionInfoDto dto) {
    boolean isNew = false;
    LambdaQueryWrapper<ChatSession> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatSession::getOwnId, userId);
    queryWrapper.eq(ChatSession::getChatType, dto.getChatType().toString());
    queryWrapper.eq(ChatSession::getTargetId, dto.getTargetId());
    ChatSession chatSession = chatSessionMapper.selectOne(queryWrapper);
    if (chatSession == null) {
      chatSession = new ChatSession();
      chatSession.setChatType(dto.getChatType().toString());
      chatSession.setOwnId(userId);
      chatSession.setTargetId(dto.getTargetId());
      isNew = true;
    }
    chatSession.setUpdateTime(System.currentTimeMillis());

    return isNew ? chatSessionMapper.insert(chatSession) > 0
        : chatSessionMapper.updateById(chatSession) > 0;
  }

  @Override
  public Set<ChatSessionInfoDto> list(Long userId) {
    LambdaQueryWrapper<ChatSession> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.eq(ChatSession::getOwnId, userId);
    queryWrapper.orderByDesc(ChatSession::getUpdateTime);
    List<ChatSession> chatSessions = chatSessionMapper.selectList(queryWrapper);
    if (CollUtil.isEmpty(chatSessions)) {
      return Collections.emptySet();
    }
    return chatSessions.stream().map(e -> {
      ChatSessionInfoDto dto = new ChatSessionInfoDto();
      dto.setCreateTime(e.getUpdateTime());
      dto.setChatType(ChatType.valueOf(e.getChatType()));
      dto.setTargetId(e.getTargetId());
      return dto;
    }).collect(Collectors.toSet());
  }

  @Override
  public boolean del(Long userId, ChatSessionInfoDto dto) {
    LambdaUpdateWrapper<ChatSession> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(ChatSession::getOwnId, userId);
    wrapper.eq(ChatSession::getChatType, dto.getChatType().toString());
    wrapper.eq(ChatSession::getTargetId, dto.getTargetId());
    chatSessionMapper.delete(wrapper);
    return true;
  }
}
