package io.xiaochangbai.muchat.sdk.task;

import io.xiaochangbai.muchat.sdk.listener.MessageListenerMulticaster;
import io.xiaochangbai.muchat.common.core.contant.RedisKey;
import io.xiaochangbai.muchat.common.core.enums.IMListenerType;
import io.xiaochangbai.muchat.common.core.model.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class PullSendResultGroupMessageTask extends  AbstractPullMessageTask{

    @Qualifier("IMRedisTemplate")
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private MessageListenerMulticaster listenerMulticaster;

    @Override
    public void pullMessage() {
        String key = RedisKey.IM_RESULT_GROUP_QUEUE;
        SendResult result =  (SendResult)redisTemplate.opsForList().leftPop(key,10, TimeUnit.SECONDS);
        if(result != null) {
            listenerMulticaster.multicast(IMListenerType.GROUP_MESSAGE,result);
        }
    }

}
