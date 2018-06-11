package com.yhqs.commons.security.config.interceptor;

import com.yhqs.commons.security.interceptor.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Arrays;
import java.util.List;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport{

    private List<String> permissionInterceptorExcludePath = Arrays.asList("/api/1.0/account/login",
                                                                          "/api/1.0/account/register");

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new PermissionInterceptor())
                .pathMatcher(new AntPathMatcher())
                .addPathPatterns("/**")
                .excludePathPatterns(permissionInterceptorExcludePath);
    }
}
