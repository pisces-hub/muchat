package io.pisceshub.muchat.connector.netty.processor;

import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import io.pisceshub.muchat.connector.task.handler.GroupMessageHandler;
import io.pisceshub.muchat.connector.task.handler.PrivateMessageHandler;

public class ProcessorFactory {

    public static MessageProcessor createProcessor(IMCmdType cmd){
        MessageProcessor  processor = null;
        switch (cmd){
            case LOGIN:
                processor = SpringContextHolder.getApplicationContext().getBean(LoginProcessor.class);
                break;
            case HEART_BEAT:
                processor = SpringContextHolder.getApplicationContext().getBean(HeartbeatProcessor.class);
                break;
            default:
                break;
        }
        return processor;
    }

}
