package io.pisceshub.muchat.connector.task;


import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.model.IMRecvInfo;
import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.connector.netty.IMServerGroup;
import io.pisceshub.muchat.connector.task.handler.MessageHandler;
import io.pisceshub.muchat.connector.task.handler.MessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class PullUnreadPrivateMessageTask extends  AbstractPullMessageTask {


    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public void pullMessage() {
        // 从redis拉取未读消息
        String key = RedisKey.IM_UNREAD_PRIVATE_QUEUE + IMServerGroup.serverId;
        List messageInfos = redisTemplate.opsForList().range(key,0,ONES_PULL_MESSAGE_COUNT);
        for(Object o: messageInfos){
            redisTemplate.opsForList().leftPop(key);
            MessageHandler messageHandler = MessageHandlerFactory.createHandler(IMCmdType.PRIVATE_MESSAGE);
            if(messageHandler!=null){
                PrivateMessageInfo recvInfo = (PrivateMessageInfo)o;
                messageHandler.handler(recvInfo);
            }
        }
    }

}
