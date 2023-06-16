package io.pisceshub.muchat.server.config;

import io.pisceshub.muchat.common.core.algorithm.RouteHandle;
import io.pisceshub.muchat.common.core.algorithm.consistenthash.ConsistentHashHandle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaochangbai
 * @date 2023-06-12 20:43
 */
@Configuration
public class AppConfig {

    @Bean
    public RouteHandle routeHandle(){
        return new ConsistentHashHandle();
    }

}
