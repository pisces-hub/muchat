package io.pisceshub.muchat.sdk.listener;

import io.pisceshub.muchat.common.core.model.SendResult;

public interface MessageListener {

    void process(SendResult result);

}
