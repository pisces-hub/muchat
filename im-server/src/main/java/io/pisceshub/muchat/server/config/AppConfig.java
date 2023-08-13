package io.pisceshub.muchat.server.config;

import io.pisceshub.muchat.server.core.algorithm.RouteHandle;
import io.pisceshub.muchat.server.core.algorithm.consistenthash.ConsistentHashHandle;
import io.pisceshub.muchat.server.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaochangbai
 * @date 2023-06-12 20:43
 */
@Slf4j
@Configuration
public class AppConfig {

  @Bean
  public RouteHandle routeHandle() {
    return new ConsistentHashHandle();
  }

}
