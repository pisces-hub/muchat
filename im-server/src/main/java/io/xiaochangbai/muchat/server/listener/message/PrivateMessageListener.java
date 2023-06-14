package io.xiaochangbai.muchat.server.listener.message;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.xiaochangbai.muchat.sdk.IMClient;
import io.xiaochangbai.muchat.sdk.annotation.IMListener;
import io.xiaochangbai.muchat.sdk.listener.MessageListener;
import io.xiaochangbai.muchat.common.core.enums.IMListenerType;
import io.xiaochangbai.muchat.common.core.enums.IMSendCode;
import io.xiaochangbai.muchat.common.core.model.PrivateMessageInfo;
import io.xiaochangbai.muchat.common.core.model.SendResult;
import io.xiaochangbai.muchat.server.entity.PrivateMessage;
import io.xiaochangbai.muchat.common.core.enums.MessageStatus;
import io.xiaochangbai.muchat.common.core.enums.MessageType;
import io.xiaochangbai.muchat.server.service.IPrivateMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


@Slf4j
@IMListener(type = IMListenerType.PRIVATE_MESSAGE)
public class PrivateMessageListener implements MessageListener {

    @Autowired
    private IPrivateMessageService privateMessageService;

    @Autowired
    private IMClient imClient;

    @Override
    public void process(SendResult result){
        PrivateMessageInfo messageInfo = (PrivateMessageInfo) result.getMessageInfo();
        // 提示类数据不记录
        if(messageInfo.getType().equals(MessageType.TIP.code())){

            return;
        }
        // 视频通话信令不记录
        if(messageInfo.getType() >= MessageType.RTC_CALL.code() && messageInfo.getType()< MessageType.RTC_CANDIDATE.code()){
            // 通知用户呼叫失败了
            if(messageInfo.getType() == MessageType.RTC_CALL.code()
                    && !result.getCode().equals(IMSendCode.SUCCESS)){
                PrivateMessageInfo sendMessage = new PrivateMessageInfo();
                sendMessage.setRecvId(messageInfo.getSendId());
                sendMessage.setSendId(messageInfo.getRecvId());
                sendMessage.setType(MessageType.RTC_FAILED.code());
                sendMessage.setContent(result.getCode().description());
                sendMessage.setSendTime(new Date());
                imClient.sendPrivateMessage(sendMessage.getRecvId(),sendMessage);
            }
        }
        // 更新消息状态
        if(result.getCode().equals(IMSendCode.SUCCESS)){
            UpdateWrapper<PrivateMessage> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda().eq(PrivateMessage::getId,messageInfo.getId())
                    .eq(PrivateMessage::getStatus, MessageStatus.UNREAD.code())
                    .set(PrivateMessage::getStatus, MessageStatus.ALREADY_READ.code());
            privateMessageService.update(updateWrapper);
            log.info("消息已读，消息id:{}，发送者:{},接收者:{}",messageInfo.getId(),messageInfo.getSendId(),messageInfo.getRecvId());
        }
    }

}
