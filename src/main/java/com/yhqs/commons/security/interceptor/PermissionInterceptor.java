package com.yhqs.commons.security.interceptor;

import com.yhqs.commons.consts.CacheName;
import com.yhqs.core.permission.entity.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wah.doraemon.security.exception.ForbiddenException;
import org.wah.doraemon.security.exception.LoginFailException;
import org.wah.doraemon.utils.RedisUtils;
import org.wah.ferryman.security.consts.HttpHeaderName;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class PermissionInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    //路径匹配器
    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String        url    = request.getRequestURI().substring(request.getContextPath().length());
        RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());

        //缓存
        ShardedJedis jedis = shardedJedisPool.getResource();

        //非需授权URL
        Set<Function> needNotAllots = RedisUtils.smembers(jedis, CacheName.NO_PERMISSION_URL, Function.class);
        if(needNotAllots != null && !needNotAllots.isEmpty()){
            for(Function needNotAllot : needNotAllots){
                if(matcher.match(needNotAllot.getUrl(), url) && needNotAllot.getMethod().equals(method)){
                    RedisUtils.close(jedis);
                    return true;
                }
            }
        }

        String ticket = request.getHeader(HttpHeaderName.AUTHORIZATION);
        if(StringUtils.isBlank(ticket)){
            throw new LoginFailException("登录失败,请重新登录");
        }

        //权限验证
        Set<Function> userFunctions = RedisUtils.smembers(jedis, CacheName.USER_FUNCTION + ticket, Function.class);
        if(userFunctions != null && !userFunctions.isEmpty()){
            for(Function function : userFunctions){
                if(matcher.match(function.getUrl(), url) && function.getMethod().equals(method)){
                    RedisUtils.close(jedis);
                    return true;
                }
            }
        }

        //删除相关缓存
        RedisUtils.delete(jedis, CacheName.USER_INFO + ticket);
        RedisUtils.delete(jedis, CacheName.USER_FUNCTION + ticket);
        RedisUtils.delete(jedis, CacheName.USER_MENU + ticket);
        RedisUtils.close(jedis);

        throw new ForbiddenException("您没有访问权限[{0}]", url);
    }
}
