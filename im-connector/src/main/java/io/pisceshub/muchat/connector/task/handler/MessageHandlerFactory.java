package io.pisceshub.muchat.connector.task.handler;

import io.pisceshub.muchat.common.core.enums.IMCmdType;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;

public class MessageHandlerFactory {

    public static MessageHandler createHandler(IMCmdType cmd){
        MessageHandler  messageHandler = null;
        switch (cmd){
            case PRIVATE_MESSAGE:
                messageHandler = SpringContextHolder.getApplicationContext().getBean(PrivateMessageHandler.class);
                break;
            case GROUP_MESSAGE:
                messageHandler = SpringContextHolder.getApplicationContext().getBean(GroupMessageHandler.class);
                break;
            default:
                break;
        }
        return messageHandler;
    }

}
