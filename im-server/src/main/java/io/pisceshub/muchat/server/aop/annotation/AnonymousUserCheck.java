package io.pisceshub.muchat.server.aop.annotation;

import java.lang.annotation.*;

/**
 * @author xiaochangbai
 * @date 2023-06-24 21:59
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonymousUserCheck {
}
