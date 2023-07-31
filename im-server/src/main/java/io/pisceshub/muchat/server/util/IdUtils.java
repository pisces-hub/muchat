package io.pisceshub.muchat.server.util;

import cn.hutool.core.lang.Snowflake;
import io.pisceshub.muchat.common.cache.AppCache;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;

/**
 * @author xiaochangbai
 * @date 2023-06-18 15:56
 */
public final class IdUtils {

  private static Snowflake snowflake = new Snowflake(0, 0);

  private static AppCache cache = SpringContextHolder.getBean(AppCache.class);

  public static String generatorId() {
    String key = "id-" + IdUtils.class.getSimpleName();
    Long aLong = cache.incr(key);
    return aLong + snowflake.nextIdStr();
  }

}
