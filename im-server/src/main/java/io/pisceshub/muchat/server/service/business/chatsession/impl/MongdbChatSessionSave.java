package io.xiaochangbai.muchat.server.service.business.chatsession.impl;

import io.xiaochangbai.muchat.server.service.business.chatsession.ChatSessionSave;
import io.xiaochangbai.muchat.server.vo.ChatSessionAddVo;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:50
 */
public class MongdbChatSessionSave implements ChatSessionSave {
    @Override
    public boolean add(ChatSessionAddVo vo) {
        return false;
    }
}
