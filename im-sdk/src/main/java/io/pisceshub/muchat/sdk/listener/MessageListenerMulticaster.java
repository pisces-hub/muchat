package io.pisceshub.muchat.sdk.listener;

import cn.hutool.core.collection.CollUtil;
import io.pisceshub.muchat.common.core.enums.IMListenerType;
import io.pisceshub.muchat.common.core.model.SendResult;
import io.pisceshub.muchat.sdk.annotation.IMListener;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class MessageListenerMulticaster {

  private List<MessageListener> messageListeners;

  public void multicast(IMListenerType type, SendResult result) {
    if (CollUtil.isEmpty(messageListeners)) {
      return;
    }
    for (MessageListener listener : messageListeners) {
      IMListener annotation = listener.getClass().getAnnotation(IMListener.class);
      if (annotation != null
          && (annotation.type().equals(IMListenerType.ALL) || annotation.type().equals(type))) {
        listener.process(result);
      }
    }
  }
}
