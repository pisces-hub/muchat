package io.pisceshub.muchat.server.service.business.chatsession;

import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;

import java.util.Set;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
public interface ChatSessionSave {

    boolean add(Long userId, ChatSessionInfoDto vo);

    Set<ChatSessionInfoDto> list(Long userId);

    boolean del(Long userId, ChatSessionInfoDto vo);
}
