package io.xiaochangbai.muchat.connector.task;

import io.xiaochangbai.muchat.common.core.contant.RedisKey;
import io.xiaochangbai.muchat.common.core.enums.IMCmdType;
import io.xiaochangbai.muchat.common.core.model.GroupMessageInfo;
import io.xiaochangbai.muchat.common.core.model.IMRecvInfo;
import io.xiaochangbai.muchat.connector.netty.IMServerGroup;
import io.xiaochangbai.muchat.connector.netty.processor.MessageProcessor;
import io.xiaochangbai.muchat.connector.netty.processor.ProcessorFactory;
import io.xiaochangbai.muchat.connector.netty.ws.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class PullUnreadGroupMessageTask extends  AbstractPullMessageTask {

    @Autowired
    private WebSocketServer WSServer;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;



    @Override
    public void pullMessage() {
        // 从redis拉取未读消息
        String key = RedisKey.IM_UNREAD_GROUP_QUEUE + IMServerGroup.serverId;
        List messageInfos = redisTemplate.opsForList().range(key,0,-1);
        for(Object o: messageInfos){
            redisTemplate.opsForList().leftPop(key);
            IMRecvInfo<GroupMessageInfo> recvInfo = (IMRecvInfo)o;
            MessageProcessor processor = ProcessorFactory.createProcessor(IMCmdType.GROUP_MESSAGE);
            processor.process(recvInfo);
        }
    }



}
