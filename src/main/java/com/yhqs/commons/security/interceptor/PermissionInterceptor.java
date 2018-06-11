package com.yhqs.commons.security.interceptor;

import com.google.gson.reflect.TypeToken;
import com.yhqs.commons.consts.CacheName;
import com.yhqs.core.permission.entity.Function;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wah.doraemon.security.exception.ForbiddenException;
import org.wah.doraemon.security.exception.LoginFailException;
import org.wah.doraemon.security.exception.TicketAuthenticationException;
import org.wah.doraemon.utils.RedisUtils;
import org.wah.ferryman.security.consts.HttpHeaderName;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

public class PermissionInterceptor extends HandlerInterceptorAdapter{

    @Getter
    @Setter
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
        List<Function> needNotAllots = RedisUtils.get(jedis, CacheName.NO_PERMISSION_URL, new TypeToken<List<Function>>(){}.getType());
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

        //查询账户ID
        String accountId = RedisUtils.get(jedis, CacheName.USER_TICKET + ticket, String.class);
        if(StringUtils.isBlank(accountId)){
            throw new TicketAuthenticationException("无效的票据凭证");
        }

        //权限验证
        List<Function> userFunctions = RedisUtils.get(jedis, CacheName.USER_FUNCTION + accountId, new TypeToken<List<Function>>(){}.getType());
        if(userFunctions != null && !userFunctions.isEmpty()){
            for(Function function : userFunctions){
                if(matcher.match(function.getUrl(), url) && function.getMethod().equals(method)){
                    //延续有效期
                    RedisUtils.expire(jedis, CacheName.USER_INFO + accountId, CacheName.LOGIN_EXPIRE);
                    RedisUtils.expire(jedis, CacheName.USER_FUNCTION + accountId, CacheName.LOGIN_EXPIRE);

                    RedisUtils.close(jedis);
                    return true;
                }
            }
        }

        //删除相关缓存
        RedisUtils.delete(jedis, CacheName.USER_TICKET + ticket);
        RedisUtils.delete(jedis, CacheName.USER_INFO + accountId);
        RedisUtils.delete(jedis, CacheName.USER_FUNCTION + accountId);
        RedisUtils.close(jedis);

        throw new ForbiddenException("您没有访问权限[{0}]", url);
    }
}
