package io.pisceshub.muchat.server.controller;

import com.alibaba.fastjson.JSONObject;
import io.pisceshub.muchat.common.core.utils.Result;
import io.pisceshub.muchat.common.core.utils.ResultUtils;
import io.pisceshub.muchat.server.common.enums.RegisterRromEnum;
import io.pisceshub.muchat.server.common.vo.user.LoginResp;
import io.pisceshub.muchat.server.config.properties.AppConfigInfo;
import io.pisceshub.muchat.server.exception.BusinessException;
import io.pisceshub.muchat.server.service.IUserService;
import io.pisceshub.muchat.server.util.OauthLoginUtils;
import io.pisceshub.muchat.server.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;


@Slf4j
@Controller
@Api(tags = "联合登录")
@RequestMapping("/connect")
public class OauthLoginController {

    @Autowired
    private AppConfigInfo appConfigInfo;

    @Autowired
    private OauthLoginUtils oauthLoginUtils;

    @Autowired
    private IUserService userService;


    @ResponseBody
    @GetMapping("/login/web/{type}")
    @ApiOperation(value = "WEB信任登录授权,包含PC、WAP")
    public Result<String> webAuthorize(@PathVariable String type) throws IOException {
        AuthRequest authRequest = oauthLoginUtils.buildAuthRequest(type);
        String uuId = UUID.randomUUID().toString().replaceAll("-","");
        String url = authRequest.authorize(uuId);
        return ResultUtils.success(url);
    }


    @ApiOperation(value = "信任登录统一回调地址", hidden = true)
    @GetMapping("/callback/{type}")
    public void callBack(@PathVariable String type, AuthCallback callback,
                           HttpServletResponse httpServletResponse) throws IOException {
        String errorMsg = "登录失败";
        try {
            AuthRequest authRequest = oauthLoginUtils.buildAuthRequest(type);
            AuthResponse response = authRequest.login(callback);
            int code = response.getCode();
            if(code==2000){
                AuthUser authUser = (AuthUser) response.getData();
                log.info("登录响应，{}", JSONObject.toJSONString(authUser));
                LoginResp vo = userService.oauthLogin(type,authUser);
                String oAuthInfo = SecurityUtils.base64Obj(JSONObject.toJSONString(vo));
                httpServletResponse.sendRedirect(appConfigInfo.getAuth2().getLoginRedirectUri()+"?state=1&oAuthInfo="+oAuthInfo);
                return;
            }
            errorMsg = response.getMsg();
        }catch (Exception e){
            errorMsg = e.getMessage();
        }
        httpServletResponse.sendRedirect(appConfigInfo.getAuth2().getLoginRedirectUri()+"?state=0&errorMsg="+SecurityUtils.base64Obj(errorMsg));
    }




}
