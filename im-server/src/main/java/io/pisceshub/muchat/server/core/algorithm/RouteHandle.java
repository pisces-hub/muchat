package io.pisceshub.muchat.server.core.algorithm;

import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;
import io.pisceshub.muchat.server.core.NodeContainer;

import java.util.List;

/**
 * Function:
 */
public interface RouteHandle {

    /**
     * 在一批服务器里进行路由
     * 
     * @param values
     * @param key
     * @return
     */
    NodeContainer.WNode routeServer(NetProtocolEnum protocolEnum, List<NodeContainer.WNode> values, String key);
}
