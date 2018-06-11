package com.yhqs.commons.security.interceptor;

import org.apache.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OptionsRequestInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String method = request.getMethod();

        if(method.equalsIgnoreCase("OPTIONS")){
            response.setStatus(HttpStatus.SC_OK);
            return false;
        }

        return true;
    }
}
