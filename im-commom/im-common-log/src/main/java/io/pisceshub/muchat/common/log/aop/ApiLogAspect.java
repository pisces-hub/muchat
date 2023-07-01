package io.pisceshub.muchat.common.log.aop;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.json.JSON;
import com.alibaba.fastjson.JSONObject;
import io.pisceshub.muchat.common.core.utils.TPair;
import io.pisceshub.muchat.common.log.annotation.ApiLog;
import io.pisceshub.muchat.common.log.entity.ApiLogEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author xiaochangbai
 * @date 2023-07-01 10:44
 */
@Aspect

public class ApiLogAspect {




    /**
     * 打印类或方法上的
     */
    @Around("@within(io.pisceshub.muchat.common.log.annotation.ApiLog) " +
            "|| @annotation(io.pisceshub.muchat.common.log.annotation.ApiLog)")
    public Object printMLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = SystemClock.now();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logger log = LoggerFactory.getLogger(methodSignature.getDeclaringType());
        String beginTime = DateUtil.now();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } finally {
            Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),
                    methodSignature.getMethod().getParameterTypes());
            ApiLog logAnnotation = Optional.ofNullable(targetMethod.getAnnotation(ApiLog.class))
                    .orElse(joinPoint.getTarget().getClass().getAnnotation(ApiLog.class));
            if (logAnnotation != null) {
                ApiLogEntity logPrint = new ApiLogEntity();
                logPrint.setBeginTime(beginTime);
                if (logAnnotation.input()) {
                    logPrint.setInputParams(buildInput(joinPoint));
                }
                if (logAnnotation.output()) {
                    logPrint.setOutputParams(result);
                }
                TPair<String,String> pair = buildApiInfo(joinPoint);
                String methodType = "", requestURI = "";
                try {
                    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    methodType = servletRequestAttributes.getRequest().getMethod();
                    requestURI = servletRequestAttributes.getRequest().getRequestURI();
                } catch (Exception ignored) {
                }
                log.info("[{}:{}]-[{}] {}, executeTime: {}ms, info: {}",pair.getLeft(),pair.getRight(), methodType,
                        requestURI, SystemClock.now() - startTime, JSONObject.toJSONString(logPrint));
            }
        }
        return result;
    }

    private TPair<String, String> buildApiInfo(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String moduleName = null;
        String apiName = null;
        Api apiAnnotation = joinPoint.getTarget().getClass().getAnnotation(Api.class);
        if(apiAnnotation!=null && apiAnnotation.tags()!=null){
            String[] tags = apiAnnotation.tags();
            moduleName = tags.length>0?tags[0]:Arrays.toString(tags);
        }

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = joinPoint.getTarget().getClass().getDeclaredMethod(methodSignature.getName(),
                methodSignature.getMethod().getParameterTypes());
        ApiOperation apiOperation = targetMethod.getAnnotation(ApiOperation.class);
        if(apiAnnotation!=null){
            apiName = apiOperation.value();
        }
        return new TPair<>(moduleName,apiName);
    }

    private Object[] buildInput(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] printArgs = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if ((args[i] instanceof HttpServletRequest) || args[i] instanceof HttpServletResponse) {
                continue;
            }
            if (args[i] instanceof byte[]) {
                printArgs[i] = "byte array";
            } else if (args[i] instanceof MultipartFile) {
                printArgs[i] = "file";
            } else {
                printArgs[i] = args[i];
            }
        }
        return printArgs;
    }


}
