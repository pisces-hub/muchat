package io.pisceshub.muchat.sdk.listener;

import cn.hutool.core.collection.CollUtil;
import io.pisceshub.muchat.common.core.enums.IMListenerType;
import io.pisceshub.muchat.common.core.model.SendResult;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import io.pisceshub.muchat.sdk.annotation.IMListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class MessageListenerMulticaster  implements CommandLineRunner {


  @SuppressWarnings("all")
  private Map<IMListenerType,List<MessageListener>> listenerMap;


  @Override
  public void run(String... args) throws Exception {
    Collection<MessageListener> listenerCollection = SpringContextHolder.getBeans(
        MessageListener.class);
    for (MessageListener listener : listenerCollection) {
      IMListener annotation = listener.getClass().getAnnotation(IMListener.class);
      if (annotation == null || annotation.type() == null) {
        continue;
      }
      IMListenerType type = annotation.type();
      List<MessageListener> messageListeners = listenerMap.getOrDefault(type, new ArrayList<>(10));
      messageListeners.add(listener);
      listenerMap.put(type, messageListeners);
    }
    log.info("listenerMap初始化完成：{}", listenerMap);
  }

  public void multicast(IMListenerType type, SendResult result) {
    if (CollUtil.isEmpty(listenerMap)) {
      return;
    }

    List<MessageListener> messageListeners = new ArrayList<>(10);
    List<MessageListener> l1 = listenerMap.get(type);
    List<MessageListener> l2 = listenerMap.get(IMListenerType.ALL);
    if(CollUtil.isNotEmpty(l1)){
      messageListeners.addAll(l1);
    }
    if(CollUtil.isNotEmpty(l2)){
      messageListeners.addAll(l2);
    }

    for (MessageListener listener : messageListeners) {
      listener.process(result);
    }
  }


}
