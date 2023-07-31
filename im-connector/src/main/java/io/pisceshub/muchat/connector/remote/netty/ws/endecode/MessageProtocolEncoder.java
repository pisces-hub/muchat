package io.pisceshub.muchat.connector.remote.netty.ws.endecode;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.pisceshub.muchat.common.core.model.IMSendInfo;

import java.util.List;

public class MessageProtocolEncoder extends MessageToMessageEncoder<IMSendInfo> {

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext, IMSendInfo sendInfo,
      List<Object> list) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String text = objectMapper.writeValueAsString(sendInfo);

    TextWebSocketFrame frame = new TextWebSocketFrame(text);
    list.add(frame);
  }
}
