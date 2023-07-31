package io.pisceshub.muchat.connector.processor;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.model.IMSendInfo;
import io.pisceshub.muchat.common.core.model.LoginInfo;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import io.pisceshub.muchat.connector.listener.event.UserEvent;
import io.pisceshub.muchat.connector.utils.SendMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HeartbeatProcessor implements MessageProcessor {

  @Override
  public void process(ChannelHandlerContext ctx, Object obj) {
    // 响应ws
    SendMessageUtils.send(ctx, IMSendInfo.create(IMCmdType.HEART_BEAT));
  }

}
