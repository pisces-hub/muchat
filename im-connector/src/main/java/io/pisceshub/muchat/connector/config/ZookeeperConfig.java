package io.pisceshub.muchat.connector.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: xiaochangbai
 * @date: 2023/7/21 11:37
 */
@Slf4j
@Configuration
public class ZookeeperConfig {

  @Autowired
  private AppConfigProperties appConfigProperties;

  @Bean(destroyMethod = "close")
  public CuratorFramework curatorFramework() {
    CuratorFramework client = CuratorFrameworkFactory.newClient(
        appConfigProperties.getZk().getAddress(),
        new RetryNTimes(10, 5000));
    client.start();
    log.info("zookeeper 服务启动完成!");
    return client;
  }

}
