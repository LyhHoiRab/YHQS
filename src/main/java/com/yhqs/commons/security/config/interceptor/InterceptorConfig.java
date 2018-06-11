package com.yhqs.commons.security.config.interceptor;

import com.yhqs.commons.security.interceptor.OptionsRequestInterceptor;
import com.yhqs.commons.security.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wah.ferryman.security.interceptor.AuthenticateInterceptor;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Arrays;
import java.util.List;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport{

    @Autowired
    private ShardedJedisPool pool;

    private List<String> permissionInterceptorExcludePath = Arrays.asList("/api/1.0/account/login",
                                                                          "/api/1.0/account/register");

    private List<String> authenticateInterceptorExcludePath = Arrays.asList("/api/1.0/account/login",
                                                                            "/api/1.0/account/register");

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(getOptionsInterceptor())
                .pathMatcher(new AntPathMatcher())
                .addPathPatterns("/**")
                .order(-1);

        registry.addInterceptor(getAuthenticateInterceptor())
                .pathMatcher(new AntPathMatcher())
                .addPathPatterns("/**")
                .excludePathPatterns(authenticateInterceptorExcludePath)
                .order(0);

        registry.addInterceptor(getPermissionInterceptor())
                .pathMatcher(new AntPathMatcher())
                .addPathPatterns("/**")
                .excludePathPatterns(permissionInterceptorExcludePath)
                .order(1);
    }

    public HandlerInterceptorAdapter getPermissionInterceptor(){
        PermissionInterceptor interceptor = new PermissionInterceptor();
        interceptor.setShardedJedisPool(pool);

        return interceptor;
    }

    public HandlerInterceptorAdapter getAuthenticateInterceptor(){
        AuthenticateInterceptor interceptor = new AuthenticateInterceptor();
        interceptor.setServer("http://localhost:8088");

        return interceptor;
    }

    public HandlerInterceptorAdapter getOptionsInterceptor(){
        OptionsRequestInterceptor interceptor = new OptionsRequestInterceptor();

        return interceptor;
    }
}
