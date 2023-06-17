package io.xiaochangbai.muchat.server.service.business.chatsession;

import io.xiaochangbai.muchat.server.vo.ChatSessionAddVo;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:49
 */
public interface ChatSessionSave {
    boolean add(ChatSessionAddVo vo);
}
