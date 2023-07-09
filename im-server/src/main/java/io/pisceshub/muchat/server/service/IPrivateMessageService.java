package io.pisceshub.muchat.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.server.common.entity.PrivateMessage;
import io.pisceshub.muchat.server.common.vo.message.MessageSendResp;
import io.pisceshub.muchat.server.common.vo.message.PrivateMessageSendReq;

import java.util.List;


public interface IPrivateMessageService extends IService<PrivateMessage> {

    MessageSendResp sendMessage(PrivateMessageSendReq vo);

    void recallMessage(Long id);

    List<PrivateMessageInfo> findHistoryMessage(Long friendId, Long lastMessageId);

    void pullUnreadMessage();

}
