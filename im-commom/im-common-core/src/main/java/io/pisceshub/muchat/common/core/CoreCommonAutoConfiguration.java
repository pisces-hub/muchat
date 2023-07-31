package io.pisceshub.muchat.common.core;

import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/6/12 16:01
 */
@Configuration
public class CoreCommonAutoConfiguration {

  @Bean
  public SpringContextHolder springContextHolder() {
    return new SpringContextHolder();
  }

}
