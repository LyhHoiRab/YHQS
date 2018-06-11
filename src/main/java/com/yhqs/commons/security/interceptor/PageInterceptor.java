package com.yhqs.commons.security.interceptor;

import com.yhqs.commons.consts.SessionName;
import eu.bitwalker.useragentutils.Manufacturer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wah.doraemon.security.exception.BrowserNotSupportException;
import org.wah.doraemon.utils.UserAgentUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PageInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //是否允许IE访问
        Boolean allowed = new Boolean(response.getHeader(SessionName.ALLOW_IE));
        if(allowed != null && allowed){
            return true;
        }

        //判断浏览器
        String userAgent = request.getHeader("User-Agent");
        Manufacturer manufacturer = UserAgentUtils.getManufacturer(userAgent);
        //微软厂商
        if(Manufacturer.MICROSOFT.equals(manufacturer)){
            throw new BrowserNotSupportException("不支持微软厂商的客户端");
        }

        return true;
    }
}
