package io.pisceshub.muchat.common.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaochangbai
 * @date 2023-07-01 10:42
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiLog {

    /**
     * 打印请求参数
     */
    boolean input() default true;

    /**
     * 打印返回值
     */
    boolean output() default true;

    /**
     * 是否持久化
     * @return
     */
    boolean isPersistence() default  false;
}
