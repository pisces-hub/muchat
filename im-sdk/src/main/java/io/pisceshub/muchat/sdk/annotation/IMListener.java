package io.pisceshub.muchat.sdk.annotation;

import io.pisceshub.muchat.common.core.enums.IMListenerType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface IMListener {

  IMListenerType type();

}
