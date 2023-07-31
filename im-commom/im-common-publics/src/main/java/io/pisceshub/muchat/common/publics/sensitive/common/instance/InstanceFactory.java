package io.pisceshub.muchat.common.publics.sensitive.common.instance;

import io.pisceshub.muchat.common.publics.sensitive.common.annotation.ThreadSafe;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.ArgUtil;
import io.pisceshub.muchat.common.publics.sensitive.common.utils.ObjectUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaochangbai
 */
public final class InstanceFactory implements Instance {

  private final Map<String, Object> singletonMap;
  private ThreadLocal<Map<String, Object>> mapThreadLocal;

  private InstanceFactory() {
    this.singletonMap = new ConcurrentHashMap();
    this.mapThreadLocal = new ThreadLocal();
  }

  public static InstanceFactory getInstance() {
    return SingletonHolder.INSTANCE_FACTORY;
  }

  public static <T> T singletion(Class<T> tClass) {
    return getInstance().singleton(tClass);
  }

  public static <T> T singletion(Class<T> tClass, String groupName) {
    return getInstance().singleton(tClass, groupName);
  }

  public <T> T singleton(Class<T> tClass, String groupName) {
    return this.getSingleton(tClass, groupName, this.singletonMap);
  }

  public <T> T singleton(Class<T> tClass) {
    this.notNull(tClass);
    return this.getSingleton(tClass, this.singletonMap);
  }

  public <T> T threadLocal(Class<T> tClass) {
    this.notNull(tClass);
    Map<String, Object> map = (Map) this.mapThreadLocal.get();
    if (ObjectUtil.isNull(map)) {
      map = new ConcurrentHashMap();
    }

    T instance = (T) this.getSingleton(tClass, (Map) map);
    this.mapThreadLocal.set(map);
    return instance;
  }

  public <T> T multiple(Class<T> tClass) {
    this.notNull(tClass);

    try {
      return tClass.newInstance();
    } catch (IllegalAccessException | InstantiationException var3) {
      throw new RuntimeException(var3);
    }
  }

  public <T> T threadSafe(Class<T> tClass) {
    return tClass.isAnnotationPresent(ThreadSafe.class) ? this.singleton(tClass)
        : this.multiple(tClass);
  }

  private <T> T getSingleton(Class<T> tClass, Map<String, Object> instanceMap) {
    this.notNull(tClass);
    String fullClassName = tClass.getName();
    T instance = (T) instanceMap.get(fullClassName);
    if (ObjectUtil.isNull(instance)) {
      instance = this.multiple(tClass);
      instanceMap.put(fullClassName, instance);
    }

    return instance;
  }

  private <T> T getSingleton(Class<T> tClass, String group, Map<String, Object> instanceMap) {
    this.notNull(tClass);
    ArgUtil.notEmpty(group, "key");
    String fullClassName = tClass.getName() + "-" + group;
    T instance = (T) instanceMap.get(fullClassName);
    if (ObjectUtil.isNull(instance)) {
      instance = this.multiple(tClass);
      instanceMap.put(fullClassName, instance);
    }

    return instance;
  }

  private void notNull(Class tClass) {
    ArgUtil.notNull(tClass, "class");
  }

  private static class SingletonHolder {

    private static final InstanceFactory INSTANCE_FACTORY = new InstanceFactory();

    private SingletonHolder() {
    }
  }
}
