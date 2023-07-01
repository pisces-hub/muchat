package io.pisceshub.muchat.common.log;

import io.pisceshub.muchat.common.log.aop.ApiLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaochangbai
 * @date 2023-07-01 11:23
 */
@Configuration
public class LogAutoConfig {

    @Bean
    public ApiLogAspect apiLogAspect(){
        return new ApiLogAspect();
    }
}
