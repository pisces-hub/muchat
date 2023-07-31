package io.pisceshub.muchat.server.interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.pisceshub.muchat.common.core.contant.AppConst;
import io.pisceshub.muchat.common.core.enums.ResultCode;
import io.pisceshub.muchat.common.core.utils.JwtUtil;
import io.pisceshub.muchat.server.exception.GlobalException;
import io.pisceshub.muchat.server.util.SessionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

;

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) throws Exception {
    // 如果不是映射到方法直接通过
    if (!(handler instanceof HandlerMethod)) {
      return true;
    }
    // 从 http 请求头中取出 token
    String token = request.getHeader("accessToken");
    if (token == null) {
      log.error("未登陆，url:{}", request.getRequestURI());
      throw new GlobalException(ResultCode.NO_LOGIN);
    }
    try {
      // 验证 token
      JwtUtil.checkSign(token, AppConst.ACCESS_TOKEN_SECRET);
    } catch (JWTVerificationException e) {
      log.error("token已失效，url:{}", request.getRequestURI());
      throw new GlobalException(ResultCode.INVALID_TOKEN);
    }
    // 存放session
    String strJson = JwtUtil.getInfo(token);
    SessionContext.UserSessionInfo userSession = JSON.parseObject(strJson,
        SessionContext.UserSessionInfo.class);
    request.setAttribute("session", userSession);
    return true;
  }
}
