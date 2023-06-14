package io.xiaochangbai.muchat.common.core.algorithm;

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
    String routeServer(List<String> values, String key) ;
}
