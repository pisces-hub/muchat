package io.pisceshub.muchat.server.service.business.chatsession;

import io.pisceshub.muchat.server.common.dto.ChatSessionInfoDto;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionAddReq;

import java.util.Set;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
public interface ChatSessionSave {
    boolean add(ChatSessionInfoDto vo);

    Set<ChatSessionInfoDto> list();

    boolean del(ChatSessionInfoDto vo);
}
