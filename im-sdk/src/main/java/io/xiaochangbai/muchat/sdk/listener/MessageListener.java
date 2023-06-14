package io.xiaochangbai.muchat.sdk.listener;


import io.xiaochangbai.muchat.common.core.model.SendResult;

public interface MessageListener {

     void process(SendResult result);

}
