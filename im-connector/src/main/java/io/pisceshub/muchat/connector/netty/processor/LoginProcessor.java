package io.pisceshub.muchat.connector.netty.processor;

import cn.hutool.core.bean.BeanUtil;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.model.IMSendInfo;
import io.pisceshub.muchat.common.core.model.LoginInfo;
import io.pisceshub.muchat.connector.contant.ConnectorConst;
import io.pisceshub.muchat.connector.netty.IMServerGroup;
import io.pisceshub.muchat.connector.netty.UserChannelCtxMap;
import io.pisceshub.muchat.connector.netty.ws.WebSocketServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class LoginProcessor implements MessageProcessor {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    @Override
    synchronized public void process(ChannelHandlerContext ctx, Object obj) {
        LoginInfo loginInfo = BeanUtil.copyProperties(obj,LoginInfo.class);

        log.info("用户登录，userId:{}",loginInfo.getUserId());
        ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(loginInfo.getUserId());
        if(context != null){
            // 不允许多地登录,强制下线
            IMSendInfo sendInfo = new IMSendInfo();
            sendInfo.setCmd(IMCmdType.FORCE_LOGUT.code());
            context.channel().writeAndFlush(sendInfo);
        }
        // 绑定用户和channel
        UserChannelCtxMap.addChannelCtx(loginInfo.getUserId(),ctx);
        // 设置用户id属性
        AttributeKey<Long> attr = AttributeKey.valueOf(ConnectorConst.USER_ID);
        ctx.channel().attr(attr).set(loginInfo.getUserId());
        // 心跳次数
        attr = AttributeKey.valueOf(ConnectorConst.HEARTBEAT_TIMES);
        ctx.channel().attr(attr).set(0L);
        // 在redis上记录每个user的channelId，15秒没有心跳，则自动过期
        String key = RedisKey.IM_USER_SERVER_ID+loginInfo.getUserId();
        redisTemplate.opsForValue().set(key, IMServerGroup.serverId, AppConst.ONLINE_TIMEOUT_SECOND, TimeUnit.SECONDS);
        // 响应ws
        IMSendInfo sendInfo = new IMSendInfo();
        sendInfo.setCmd(IMCmdType.LOGIN.code());
        ctx.channel().writeAndFlush(sendInfo);
    }
}
