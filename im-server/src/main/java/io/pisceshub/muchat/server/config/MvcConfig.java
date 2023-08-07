package io.pisceshub.muchat.server.config;

import io.pisceshub.muchat.server.interceptor.AuthInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(authInterceptor())
        .addPathPatterns("/**")
        .excludePathPatterns("/login", "/logout", "/register", "/refreshToken","/anonymousLogin", "/connect/**")
        // swagger
        .excludePathPatterns("/doc.html")
        .excludePathPatterns("/swagger-ui.html")
        .excludePathPatterns("/swagger-resources/**")
        .excludePathPatterns("/webjars/**")
        .excludePathPatterns("/*/api-docs")
        .excludePathPatterns("/connector/**");

  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");

    /** 配置knife4j 显示文档 */
    registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");

    /**
     * 配置swagger-ui显示文档
     */
    registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
    /** 公共部分内容 */
    registry.addResourceHandler("/webjars/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");

  }

  @Bean
  public AuthInterceptor authInterceptor() {
    return new AuthInterceptor();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    // 使用BCrypt加密密码
    return new BCryptPasswordEncoder();
  }

  @Bean
  public FilterRegistrationBean<CorsFilter> corsFilter() {
    FilterRegistrationBean<CorsFilter> corsFilterFilterRegistrationBean = new FilterRegistrationBean<>();
    // 添加CORS配置信息
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    // 允许的域，不要写*，否则cookie就无法使用了
    corsConfiguration.addAllowedOriginPattern("*");
    // 允许的头信息
    corsConfiguration.addAllowedHeader("*");
    // 允许的请求方式
    corsConfiguration.setAllowedMethods(Arrays.asList("POST", "PUT", "GET", "OPTIONS", "DELETE"));
    // 是否发送cookie信息
    corsConfiguration.setAllowCredentials(true);
    // 预检请求的有效期，单位为秒
    corsConfiguration.setMaxAge(3600L);

    // 添加映射路径，标识待拦截的请求
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    corsFilterFilterRegistrationBean.setFilter(new CorsFilter(source));
    corsFilterFilterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return corsFilterFilterRegistrationBean;
  }

}
