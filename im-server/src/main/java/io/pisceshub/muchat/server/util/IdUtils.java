package io.pisceshub.muchat.server.util;

import cn.hutool.core.lang.Snowflake;
import io.pisceshub.muchat.common.core.utils.SpringContextHolder;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author xiaochangbai
 * @date 2023-06-18 15:56
 */
public final class IdUtils {

    private static Snowflake snowflake = new Snowflake(0,0);

    private static RedisTemplate redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);

    public static String generatorId(){
        String key = "id-"+IdUtils.class.getSimpleName();
        Long aLong = redisTemplate.opsForValue().increment(key);
        return String.valueOf(aLong)+snowflake.nextIdStr();
    }

}
