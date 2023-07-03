package io.pisceshub.muchat.server.tcp.algorithm;

import io.pisceshub.muchat.common.core.enums.NetProtocolEnum;

import java.util.List;

/**
 * Function:
 */
public interface RouteHandle {

    /**
     * 在一批服务器里进行路由
     * @param values
     * @param key
     * @return
     */
    String routeServer(NetProtocolEnum protocolEnum, List<String> values, String key) ;
}
