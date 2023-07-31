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

  @Bean
  public Searcher searcher() throws IOException {
    ClassPathResource classPathResource = new ClassPathResource("/ip/ip2region.xdb");
    InputStream inputStream = classPathResource.getInputStream();
    if (inputStream == null) {
      throw new RuntimeException("初始化ip信息失败，文件不存在");
    }

    byte[] cBuff;
    try {
      cBuff = FileUtil.readToByte(inputStream);
    } catch (Exception e) {
      throw new RuntimeException("初始化ip信息失败1,", e);
    }

    // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
    Searcher searcher;
    try {
      searcher = Searcher.newWithBuffer(cBuff);
      return searcher;
    } catch (Exception e) {
      log.info("初始化ip信息失败2,", e);
      throw new RuntimeException("初始化ip信息失败2", e);
    }

  }

}
