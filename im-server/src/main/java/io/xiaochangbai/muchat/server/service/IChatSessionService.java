package io.xiaochangbai.muchat.server.service;

import io.xiaochangbai.muchat.server.vo.ChatSessionAddVo;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:45
 */
public interface IChatSessionService {
    boolean save(ChatSessionAddVo vo);
}
