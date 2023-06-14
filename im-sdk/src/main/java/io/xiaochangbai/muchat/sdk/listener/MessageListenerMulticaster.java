package io.xiaochangbai.muchat.sdk.listener;


import io.xiaochangbai.muchat.sdk.annotation.IMListener;
import io.xiaochangbai.muchat.common.core.enums.IMListenerType;
import io.xiaochangbai.muchat.common.core.model.SendResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class MessageListenerMulticaster {

    @Autowired(required = false)
    private List<MessageListener>  messageListeners  = Collections.emptyList();

    public  void multicast(IMListenerType type, SendResult result){
        for(MessageListener listener:messageListeners){
            IMListener annotation = listener.getClass().getAnnotation(IMListener.class);
            if(annotation!=null && (annotation.type().equals(IMListenerType.ALL) || annotation.type().equals(type))){
                listener.process(result);
            }
        }
    }
}
