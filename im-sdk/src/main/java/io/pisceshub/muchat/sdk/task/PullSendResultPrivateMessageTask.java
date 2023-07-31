package io.pisceshub.muchat.sdk.task;

import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.enums.IMListenerType;
import io.pisceshub.muchat.common.core.model.SendResult;
import io.pisceshub.muchat.sdk.listener.MessageListenerMulticaster;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

;

@Slf4j
@Component
public class PullSendResultPrivateMessageTask extends AbstractPullMessageTask {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  @Autowired
  private MessageListenerMulticaster listenerMulticaster;

  @Override
  public void pullMessage() {
    String key = RedisKey.IM_RESULT_PRIVATE_QUEUE;
    SendResult result = (SendResult) redisTemplate.opsForList().leftPop(key, 10, TimeUnit.SECONDS);
    if (result != null) {
      listenerMulticaster.multicast(IMListenerType.PRIVATE_MESSAGE, result);
    }
  }

}
