package io.pisceshub.muchat.server.session;

import io.xiaochangbai.muchat.common.core.enums.ResultCode;
import io.xiaochangbai.muchat.server.exception.GlobalException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/*
 * @Description
 * @Author Blue
 * @Date 2022/10/21
 */
public class SessionContext {


    public static UserSession getSession(){
        // 从请求上下文里获取Request对象
        ServletRequestAttributes requestAttributes = ServletRequestAttributes.class.
                cast(RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();
        UserSession userSession = (UserSession) request.getAttribute("session");
        return  userSession;
    }


    public static Long getUserId(){
        UserSession session = getSession();
        if(session==null){
            throw new GlobalException(ResultCode.INVALID_TOKEN);
        }
        return session.getId();
    }

}
