package io.pisceshub.muchat.server.adapter;

import io.pisceshub.muchat.common.publics.sensitive.core.config.SensitiveWordConfig;
import io.pisceshub.muchat.common.publics.sensitive.core.main.SWDispatcher;
import io.pisceshub.muchat.common.publics.sensitive.core.main.SWDispatcherDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author xiaochangbai
 * @date 2023-07-09 22:09
 */
@Component
@Configuration
public class SensitiveWordAdapter {

  @Autowired
  private SWDispatcher swDispatcher;

  /**
   * 替换敏感词
   *
   * @param target
   * @return
   */
  public String replace(final String target) {
    return swDispatcher.replace(target, '*');
  }

  @Bean
  public SWDispatcher sWDispatcher() {
    SensitiveWordConfig sensitiveWordConfig = SensitiveWordConfig.defaultConfig();
    return SWDispatcherDefault.newInstance(sensitiveWordConfig);
  }

}
