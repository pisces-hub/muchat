package io.pisceshub.muchat.sdk;

import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.sdk.sender.IMSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class IMClient {

  @Autowired
  private IMSender imSender;

  /**
   * 发送私聊消息
   *
   * @param recvId      接收用户id
   * @param messageInfo 消息体，将转成json发送到客户端
   */
  public void sendPrivateMessage(Long recvId, PrivateMessageInfo... messageInfo) {
    imSender.sendPrivateMessage(recvId, messageInfo);
  }

  /**
   * 发送群聊消息
   *
   * @param recvIds     群聊用户id列表
   * @param messageInfo 消息体，将转成json发送到客户端
   */
  public void sendGroupMessage(List<Long> recvIds, GroupMessageInfo... messageInfo) {
    imSender.sendGroupMessage(recvIds, messageInfo);
  }

}
