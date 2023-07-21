package io.pisceshub.muchat.connector.processor;

import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.model.IMSendInfo;
import io.pisceshub.muchat.connector.contant.ConnectorConst;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.pisceshub.muchat.connector.utils.SendMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class HeartbeatProcessor implements MessageProcessor {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    public void process(ChannelHandlerContext ctx, Object obj) {
        // 响应ws
        SendMessageUtils.send(ctx,IMSendInfo.create(IMCmdType.HEART_BEAT));
//        // 设置属性
//        AttributeKey<Long> attr = AttributeKey.valueOf(ConnectorConst.HEARTBEAT_TIMES);
//        Long heartbeatTimes = ctx.channel().attr(attr).get();
//        ctx.channel().attr(attr).set(++heartbeatTimes);
//        if(heartbeatTimes%10 == 0){
//            // 每心跳10次，用户在线状态续一次命
//            attr = AttributeKey.valueOf(ConnectorConst.USER_ID);
//            Long userId = ctx.channel().attr(attr).get();
//            String key = RedisKey.IM_USER_SERVER_ID+userId;
//            redisTemplate.expire(key, AppConst.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
//        }
    }

}
