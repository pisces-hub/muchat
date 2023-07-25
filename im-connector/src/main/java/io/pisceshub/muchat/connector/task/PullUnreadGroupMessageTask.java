package io.pisceshub.muchat.connector.task;

import io.pisceshub.muchat.common.cache.AppCache;
import io.pisceshub.muchat.common.core.contant.RedisKey;
import io.pisceshub.muchat.common.core.model.GroupMessageInfo;
import io.pisceshub.muchat.connector.remote.IMServerGroup;
import io.pisceshub.muchat.connector.task.handler.GroupMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PullUnreadGroupMessageTask extends AbstractPullMessageTask {


    @Autowired
    private AppCache appCache;

    @Autowired
    private GroupMessageHandler messageHandler;

    @Override
    public void pullMessage() {
        String key = RedisKey.IM_UNREAD_GROUP_QUEUE + IMServerGroup.serverId;
        List<Object> messageInfos = appCache.listPop(key, ONES_PULL_MESSAGE_COUNT);
        for(Object o: messageInfos){
            GroupMessageInfo recvInfo = (GroupMessageInfo)o;
            messageHandler.handler(recvInfo);
        }
    }

}
