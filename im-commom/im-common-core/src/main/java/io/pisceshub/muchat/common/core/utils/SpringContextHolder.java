package io.pisceshub.muchat.common.core.utils;

import java.util.Collection;
import java.util.Map;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextHolder implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextHolder.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    assertApplicationContext();
    return applicationContext;
  }

  public static void removeBean(String beanName) {
    assertApplicationContext();
    BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext
        .getAutowireCapableBeanFactory();
    beanDefinitionRegistry.getBeanDefinition(beanName);
    beanDefinitionRegistry.removeBeanDefinition(beanName);
  }

  public static void sendEvent(Object obj) {
    applicationContext.publishEvent(obj);
  }

  public static <T> T getBean(String beanName) {
    assertApplicationContext();
    return (T) applicationContext.getBean(beanName);
  }

  public static <T> T getBean(Class<T> requiredType) {
    assertApplicationContext();
    return applicationContext.getBean(requiredType);
  }

  public static <T> Collection<T> getBeans(Class<T> requiredType) {
    assertApplicationContext();
    Map<String, T> beansOfTypes = applicationContext.getBeansOfType(requiredType);
    return beansOfTypes.values();
  }

  private static void assertApplicationContext() {
    if (SpringContextHolder.applicationContext == null) {
      throw new RuntimeException(
          "applicaitonContext属性为null,请检查是否注入了SpringContextHolder!");
    }
  }

}
