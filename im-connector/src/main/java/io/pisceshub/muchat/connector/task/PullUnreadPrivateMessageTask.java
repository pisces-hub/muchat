package io.pisceshub.muchat.connector.task;


import io.pisceshub.muchat.common.cache.AppCache;
import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.model.PrivateMessageInfo;
import io.pisceshub.muchat.connector.remote.IMServerGroup;
import io.pisceshub.muchat.connector.task.handler.PrivateMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class PullUnreadPrivateMessageTask extends  AbstractPullMessageTask {

    @Autowired
    private AppCache appCache;

    @Autowired
    private PrivateMessageHandler messageHandler;

    @Override
    public void pullMessage() {
        if(IMServerGroup.serverId<0){
            return;
        }
        String key = RedisKey.IM_UNREAD_PRIVATE_QUEUE + IMServerGroup.serverId;
        // 从redis拉取未读消息
        List<Object> messageInfos = appCache.listPop(key, ONES_PULL_MESSAGE_COUNT);
        for(Object o: messageInfos){
            if(messageHandler!=null){
                PrivateMessageInfo recvInfo = (PrivateMessageInfo)o;
                messageHandler.handler(recvInfo);
            }
        }
    }

}
