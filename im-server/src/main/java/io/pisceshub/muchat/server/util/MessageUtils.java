package io.pisceshub.muchat.server.util;

import io.pisceshub.muchat.common.core.enums.MessageType;

/**
 * @author xiaochangbai
 * @date 2023-07-09 16:02
 */
public class MessageUtils {

    public static String converMessageContent(Integer type, String resourceContent) {
        if (type == null) {
            return resourceContent;
        }
        /**
         * if(msgInfo.type == 1){ chat.lastContent = "[图片]"; }else if(msgInfo.type ==
         * 2){ chat.lastContent = "[文件]"; }else if(msgInfo.type == 3){ chat.lastContent
         * = "[语音]"; }else{ chat.lastContent = msgInfo.content; }
         */
        MessageType messageType = MessageType.findByCode(type);
        if (messageType == null) {
            return resourceContent;
        }
        switch (messageType) {
            case TEXT:
                return resourceContent;
            default:
                return "[" + messageType.description() + "]";
        }
    }

}
