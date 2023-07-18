package io.pisceshub.muchat.server.util;

import com.baomidou.mybatisplus.annotation.TableField;
import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.server.exception.GlobalException;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/*
 * @Description
 * @Author Blue
 * @Date 2022/10/21
 */
public class SessionContext {


    public static UserSessionInfo getSession(){
        // 从请求上下文里获取Request对象
        ServletRequestAttributes requestAttributes = ServletRequestAttributes.class.
                cast(RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();
        UserSessionInfo userSession = (UserSessionInfo) request.getAttribute("session");
        return  userSession;
    }

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes = ServletRequestAttributes.class.
                cast(RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();
        return request;
    }


    public static Long getUserId(){
        UserSessionInfo session = getSession();
        if(session==null){
            throw new GlobalException(ResultCode.INVALID_TOKEN);
        }
        return session.getId();
    }

    public static Long getUserIdIfExist(){
        UserSessionInfo session = getSession();
        if(session==null){
            return null;
        }
        return session.getId();
    }

    @Data
    public static class UserSessionInfo {

        private Long id;
        private String userName;
        private String nickName;

        /**
         * 账号类型:0正常，1匿名
         */
        private Integer accountType;
    }
}
