package io.pisceshub.muchat.connector.processor;

import com.alibaba.fastjson.JSONObject;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.model.IMSendInfo;
import io.pisceshub.muchat.common.core.model.LoginInfo;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import io.pisceshub.muchat.connector.contant.ConnectorConst;
import io.pisceshub.muchat.connector.listener.event.UserOnlineStateEvent;
import io.pisceshub.muchat.connector.netty.UserChannelCtxMap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import io.pisceshub.muchat.connector.utils.SendMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class LoginProcessor implements MessageProcessor {


    @Override
    synchronized public void process(ChannelHandlerContext ctx, Object obj) {
        LoginInfo loginInfo =  JSONObject.parseObject(JSONObject.toJSONString(obj),LoginInfo.class);

        Long userId = parseUserId(ctx,loginInfo.getToken());

        log.info("用户登录，userId:{}",userId);
        ChannelHandlerContext context = UserChannelCtxMap.getChannelCtx(userId);
        if(context != null){
            // 不允许多地登录,强制下线
            SendMessageUtils.send(ctx,IMSendInfo.create(IMCmdType.FORCE_LOGUT,"强制下线"));
            SendMessageUtils.close(ctx);
        }
        // 绑定用户和channel
        UserChannelCtxMap.addChannelCtx(userId,ctx);
        // 设置用户id属性
        ctx.channel().attr(AttributeKey.valueOf(ConnectorConst.USER_ID)).set(userId);
        // 心跳次数
        ctx.channel().attr(AttributeKey.valueOf(ConnectorConst.HEARTBEAT_TIMES)).set(0L);
        // 在redis上记录每个user的channelId，15秒没有心跳，则自动过期
        SpringContextHolder.sendEvent(UserOnlineStateEvent.builder().userId(userId).event(UserOnlineStateEvent.Event.ONLINE).ctx(ctx).build());
    }
}
