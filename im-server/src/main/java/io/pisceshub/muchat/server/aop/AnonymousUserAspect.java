package io.pisceshub.muchat.server.aop;

import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.server.aop.annotation.AnonymousUserCheck;
import io.pisceshub.muchat.server.common.enums.UserEnum;
import io.pisceshub.muchat.server.exception.BusinessException;
import io.pisceshub.muchat.server.util.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author xiaochangbai
 * @date 2023-06-24 22:01
 */
@Slf4j
@Aspect
@Component
@Order(0)
public class AnonymousUserAspect {

    @Before("@within(io.pisceshub.muchat.server.aop.annotation.AnonymousUserCheck) " +
            "|| @annotation(io.pisceshub.muchat.server.aop.annotation.AnonymousUserCheck)")
    public void interceptor(JoinPoint point) throws NoSuchMethodException {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method targetMethod = point.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),
                methodSignature.getMethod().getParameterTypes());
        AnonymousUserCheck anonymousUserCheck = Optional.ofNullable(targetMethod.getAnnotation(AnonymousUserCheck.class))
                .orElse(point.getTarget().getClass().getAnnotation(AnonymousUserCheck.class));
        if(anonymousUserCheck==null){
            return;
        }
        SessionContext.UserSessionInfo session = SessionContext.getSession();
        if(session==null){
            throw new BusinessException(ResultCode.INVALID_TOKEN);
        }
        if(UserEnum.AccountType.Anonymous.getCode().equals(session.getAccountType())){
            throw new BusinessException(ResultCode.ANONYMOUSE_USER_NO_ACTION);
        }
    }

}
