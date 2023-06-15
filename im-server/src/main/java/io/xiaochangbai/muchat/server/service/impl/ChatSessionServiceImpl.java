package io.xiaochangbai.muchat.server.service.impl;

import io.xiaochangbai.muchat.server.service.IChatSessionService;
import io.xiaochangbai.muchat.server.service.business.chatsession.ChatSessionSave;
import io.xiaochangbai.muchat.server.vo.ChatSessionAddVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaochangbai
 * @date 2023-06-15 21:45
 */
@Service
public class ChatSessionServiceImpl implements IChatSessionService {

    @Autowired
    private ChatSessionSave chatSessionSave;

    @Override
    public boolean save(ChatSessionAddVo vo) {

        //todo 校验对于id和类型的合法性
        return chatSessionSave.add(vo);
    }

}
