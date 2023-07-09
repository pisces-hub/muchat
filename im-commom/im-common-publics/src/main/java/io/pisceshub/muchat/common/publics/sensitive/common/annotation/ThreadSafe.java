package io.pisceshub.muchat.common.publics.sensitive.common.annotation;

import java.lang.annotation.*;

/**
 * @author xiaochangbai
 */
@Documented
@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThreadSafe {
}
