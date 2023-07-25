package io.pisceshub.muchat.server.service;

import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionAddReq;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionInfoResp;
import io.pisceshub.muchat.server.common.vo.user.ChatSessionUpdateReq;

import java.util.Set;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:45
 */
public interface IChatSessionService {
    boolean save(Long userId,ChatSessionAddReq vo);

    Result<Set<ChatSessionInfoResp>> list();

    boolean del(ChatSessionUpdateReq vo);
}
