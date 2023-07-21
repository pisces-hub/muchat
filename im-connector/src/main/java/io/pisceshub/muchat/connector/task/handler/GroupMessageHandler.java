package io.pisceshub.muchat.connector.task.handler;

import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.enums.IMSendCode;
import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.common.core.model.IMRecvInfo;
import io.pisceshub.muchat.common.core.model.IMSendInfo;
import io.pisceshub.muchat.common.core.model.SendResult;
import io.pisceshub.muchat.connector.netty.UserChannelCtxMap;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class GroupMessageHandler implements MessageHandler<GroupMessageInfo> {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void handler(GroupMessageInfo messageInfo) {
        List<Long> recvIds = messageInfo.getRecvIds();
        log.info("接收到群消息，发送者:{},群id:{},接收id:{}，内容:{}",messageInfo.getSendId(),messageInfo.getGroupId(),recvIds,messageInfo.getContent());
        for(Long recvId:recvIds){
            IMSendCode code = null;
            try {
                ChannelHandlerContext channelCtx = UserChannelCtxMap.getChannelCtx(recvId);
                if(channelCtx != null && channelCtx.channel().isOpen()){
                    // 推送消息到用户
                    IMSendInfo<Object> sendInfo = IMSendInfo.builder().cmd(IMCmdType.GROUP_MESSAGE.code()).data(messageInfo).build();
                    channelCtx.channel().writeAndFlush(sendInfo);
                    code = IMSendCode.SUCCESS;
                }else {
                    // 消息发送失败确认
                    code = IMSendCode.NOT_FIND_CHANNEL;
                    log.error("未找到WS连接,发送者:{},群id:{},接收id:{}，内容:{}",messageInfo.getSendId(),messageInfo.getGroupId(),recvIds,messageInfo.getContent());
                }
            }catch (Exception e){
                // 消息发送失败确认
                code = IMSendCode.UNKONW_ERROR;
                log.error("发送消息异常,发送者:{},群id:{},接收id:{}，内容:{}",messageInfo.getSendId(),messageInfo.getGroupId(),recvIds,messageInfo.getContent());
            }
            // 消息发送成功确认
            SendResult<Object> sendResult = SendResult.builder().code(code).recvId(recvId).messageInfo(messageInfo).build();
            redisTemplate.opsForList().rightPush(RedisKey.IM_RESULT_GROUP_QUEUE,sendResult);
        }
    }
}
