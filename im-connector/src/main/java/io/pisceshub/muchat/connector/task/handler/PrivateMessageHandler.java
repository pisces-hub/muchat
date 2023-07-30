package io.pisceshub.muchat.connector.task.handler;

import io.netty.channel.ChannelHandlerContext;
import io.pisceshub.muchat.common.cache.AppCache;
import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.enums.IMSendCode;
import io.pisceshub.muchat.common.core.model.IMSendInfo;
import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.common.core.model.SendResult;
import io.pisceshub.muchat.connector.remote.netty.UserChannelCtxMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrivateMessageHandler implements MessageHandler<PrivateMessageInfo> {

    @Autowired
    private AppCache appCache;

    @Override
    public void handler(PrivateMessageInfo messageInfo) {
        Long recvId = messageInfo.getRecvId();
        log.info("接收到消息，发送者:{},接收者:{}，内容:{}", messageInfo.getSendId(), recvId, messageInfo.getContent());
        IMSendCode code = null;
        try {
            ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(recvId);
            if (channelCtx != null && channelCtx.channel().isOpen()) {
                // 推送消息到用户
                IMSendInfo<Object> imSendInfo = IMSendInfo.builder()
                    .cmd(IMCmdType.PRIVATE_MESSAGE.code())
                    .data(messageInfo)
                    .build();
                channelCtx.channel().writeAndFlush(imSendInfo);
                // 消息发送成功确认
                code = IMSendCode.SUCCESS;
            } else {
                // 消息推送失败确认
                code = IMSendCode.NOT_FIND_CHANNEL;
                log.error("未找到WS连接，发送者:{},接收者:{}，内容:{}", messageInfo.getSendId(), recvId, messageInfo.getContent());
            }
        } catch (Exception e) {
            // 消息推送失败确认
            code = IMSendCode.UNKONW_ERROR;
            log.error("发送异常，发送者:{},接收者:{}，内容:{}", messageInfo.getSendId(), recvId, messageInfo.getContent(), e);
        }
        SendResult<Object> sendResult = SendResult.builder().recvId(recvId).messageInfo(messageInfo).code(code).build();
        appCache.listPush(RedisKey.IM_RESULT_PRIVATE_QUEUE, sendResult);
    }

}
