package com.yhqs.commons.security.interceptor;

import com.google.gson.reflect.TypeToken;
import com.yhqs.commons.consts.CacheName;
import com.yhqs.core.permission.consts.ResourceType;
import com.yhqs.core.permission.dao.FunctionDao;
import com.yhqs.core.permission.dao.PermissionDao;
import com.yhqs.core.permission.dao.RoleDao;
import com.yhqs.core.permission.entity.Function;
import com.yhqs.core.permission.entity.Permission;
import com.yhqs.core.permission.entity.Role;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.wah.doraemon.entity.User;
import org.wah.doraemon.security.exception.ForbiddenException;
import org.wah.doraemon.security.exception.LoginFailException;
import org.wah.doraemon.utils.ObjectUtils;
import org.wah.doraemon.utils.RedisUtils;
import org.wah.ferryman.security.consts.HttpHeaderName;
import org.wah.ferryman.utils.AccountUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionInterceptor extends HandlerInterceptorAdapter{

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private FunctionDao functionDao;

    @Getter
    @Setter
    private ShardedJedisPool shardedJedisPool;

    //路径匹配器
    private AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //缓存
        ShardedJedis jedis = shardedJedisPool.getResource();

        try{
            String        url    = request.getRequestURI().substring(request.getContextPath().length());
            RequestMethod method = RequestMethod.valueOf(request.getMethod().toUpperCase());

            //非需授权URL
            List<Function> needNotAllots = RedisUtils.get(jedis, CacheName.NO_PERMISSION_URL, new TypeToken<List<Function>>(){}.getType());
            if(needNotAllots != null && !needNotAllots.isEmpty()){
                for(Function needNotAllot : needNotAllots){
                    if(matcher.match(needNotAllot.getUrl(), url) && needNotAllot.getMethod().equals(method)){
                        return true;
                    }
                }
            }

            String ticket = request.getHeader(HttpHeaderName.AUTHORIZATION);
            if(StringUtils.isBlank(ticket)){
                throw new LoginFailException("登录失败,请重新登录");
            }

            User user = RedisUtils.get(jedis, CacheName.USER_INFO + ticket, User.class);
            if(user == null){
                user = AccountUtils.getUser(ticket);
                //缓存
                RedisUtils.save(jedis, CacheName.USER_INFO + ticket, user, CacheName.LOGIN_EXPIRE);
            }

            //查询权限
            List<Function> userFunctions = RedisUtils.get(jedis, CacheName.USER_FUNCTION + ticket, new TypeToken<List<Function>>(){}.getType());
            if(userFunctions == null || userFunctions.isEmpty()){
                //查询权限
                Set<Permission> permissions = new HashSet<Permission>();
                permissions.addAll(permissionDao.findByAccountId(user.getAccountId(), ResourceType.FUNCTION));

                List<Role> roles = roleDao.findByAccountId(user.getAccountId());
                if(roles != null && !roles.isEmpty()){
                    permissions.addAll(permissionDao.findByRoleIds(ObjectUtils.ids(roles), ResourceType.FUNCTION));
                }

                //所有功能
                Set<String> functionIds = new HashSet<String>();
                if(permissions != null && !permissions.isEmpty()){
                    for(Permission permission : permissions){
                        functionIds.add(permission.getResourceId());
                    }
                }

                if(functionIds == null || functionIds.isEmpty()){
                    userFunctions = functionDao.find(null, null, null, new ArrayList<String>(functionIds));
                    //没有权限
                    if(userFunctions == null || userFunctions.isEmpty()){
                        throw new ForbiddenException("您没有访问权限[{0}]", url);
                    }
                    //缓存
                    RedisUtils.save(jedis, CacheName.USER_FUNCTION + ticket, userFunctions, CacheName.LOGIN_EXPIRE);
                }
            }

            //权限验证
            for(Function function : userFunctions){
                if(matcher.match(function.getUrl(), url) && function.getMethod().equals(method)){
                    //延续有效期
                    RedisUtils.expire(jedis, CacheName.USER_INFO + ticket, CacheName.LOGIN_EXPIRE);
                    RedisUtils.expire(jedis, CacheName.USER_FUNCTION + ticket, CacheName.LOGIN_EXPIRE);
                    return true;
                }
            }

            throw new ForbiddenException("您没有访问权限[{0}]", url);
        }finally{
            RedisUtils.close(jedis);
        }
    }
}
