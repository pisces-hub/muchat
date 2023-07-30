package io.pisceshub.muchat.server.core.algorithm.loop;

import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.core.NodeContainer;
import io.pisceshub.muchat.server.core.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Function:
 */
public class LoopHandle implements RouteHandle {

    private AtomicLong index = new AtomicLong();

    @Override
    public NodeContainer.WNode routeServer(NetProtocolEnum protocolEnum, List<NodeContainer.WNode> values, String key) {
        if (values.size() == 0) {
            throw new RuntimeException("");
        }
        Long position = index.incrementAndGet() % values.size();
        if (position < 0) {
            position = 0L;
        }
        return values.get(position.intValue());
    }

}
