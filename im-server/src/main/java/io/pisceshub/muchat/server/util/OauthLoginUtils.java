package io.pisceshub.muchat.server.util;

import com.alibaba.fastjson.JSONObject;
import io.pisceshub.muchat.server.common.enums.UserEnum;
import io.pisceshub.muchat.server.config.properties.AppConfigInfo;
import io.pisceshub.muchat.server.exception.BusinessException;
import me.zhyd.oauth.AuthRequestBuilder;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OauthLoginUtils {

    @Autowired
    private AppConfigInfo appConfigInfo;

    public static String parseBio(JSONObject rawUserInfo) {
        try {
            String b1 = rawUserInfo.getString("bio");
            return b1;
        } catch (Exception e) {

        }
        return null;
    }

    public AuthRequest buildAuthRequest(String type) {
        UserEnum.RegisterRromEnum registerRromEnum = UserEnum.RegisterRromEnum.findByMsg(type);
        if (registerRromEnum == null) {
            throw new BusinessException("未知的登录方式");
        }

        switch (registerRromEnum) {
            case GITEE:
                AppConfigInfo.Oauth2.Oauth2Node gitee = appConfigInfo.getAuth2().getGitee();
                return AuthRequestBuilder.builder().source("gitee").authConfig((source) -> {
                    // 通过 source 动态获取 AuthConfig
                    // 此处可以灵活的从 sql 中取配置也可以从配置文件中取配置
                    return AuthConfig.builder()
                        .clientId(gitee.getClientId())
                        .clientSecret(gitee.getClientSecret())
                        .redirectUri(gitee.getRedirectUri())
                        .build();
                }).build();
            case GITHUB:
                AppConfigInfo.Oauth2.Oauth2Node github = appConfigInfo.getAuth2().getGithub();
                AuthRequest authRequest = new AuthGithubRequest(AuthConfig.builder()
                    .clientId(github.getClientId())
                    .clientSecret(github.getClientSecret())
                    .redirectUri(github.getRedirectUri())
                    .build());
                return authRequest;
        }
        throw new BusinessException("暂不支持该方式");
    }
}
