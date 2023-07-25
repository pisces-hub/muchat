package io.pisceshub.muchat.server.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Configuration
@EnableKnife4j
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private Boolean enabled;

    @Bean
    @SuppressWarnings("all")
    public Docket createRestApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .enable(enabled)
                .apiInfo(apiInfo())
                .select() //.paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }



    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("幕聊api文档")
                .description("web")
                .version("1.0")
                .build();
    }

    private List<ApiKey> securitySchemes() {
        //设置请求头信息
        List<ApiKey> result = new ArrayList<ApiKey>();
        ApiKey apiKey = new ApiKey("accessToken", "accessToken", "header");
        result.add(apiKey);
        return result;
    }

    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
//                        .forPaths(PathSelectors.regex("^(?!auth).*$"))
                        .forPaths(PathSelectors.any())
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<SecurityReference>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorization", authorizationScopes));
        return result;
    }

}

