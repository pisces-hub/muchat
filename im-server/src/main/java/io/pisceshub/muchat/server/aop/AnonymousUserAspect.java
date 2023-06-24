package io.pisceshub.muchat.server.aop;

import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.server.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author xiaochangbai
 * @date 2023-06-24 22:01
 */
@Slf4j
@Aspect
@Component
@Order(0)
public class AnonymousUserAspect {

    @Pointcut("@annotation(io.pisceshub.muchat.server.aop.annotation.AnonymousUserCheck)")
    public void requestPointcut() {
    }

    @Before("requestPointcut()")
    public void interceptor(ProceedingJoinPoint pjp){
        throw new BusinessException(ResultCode.ANONYMOUSE_USER_NO_ACTION);
    }

}
