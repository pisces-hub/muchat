package io.pisceshub.muchat.common.core.algorithm.consistenthash;


import io.pisceshub.muchat.common.core.algorithm.RouteHandle;

import java.util.List;


public class ConsistentHashHandle implements RouteHandle {

    @Override
    public String routeServer(List<String> values, String key) {
        AbstractConsistentHash hash = new TreeMapConsistentHash(200);
        return hash.process(values, key);
    }
}
