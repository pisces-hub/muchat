package io.xiaochangbai.muchat.common.core.algorithm.random;



import io.xiaochangbai.muchat.common.core.algorithm.RouteHandle;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Function: 路由策略， 随机
 */
public class RandomHandle implements RouteHandle {

    @Override
    public String routeServer(List<String> values, String key) {
        int size = values.size();
        if (size == 0) {
            throw new RuntimeException() ;
        }
        int offset = ThreadLocalRandom.current().nextInt(size);

        return values.get(offset);
    }


}
