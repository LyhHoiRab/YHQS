package com.yhqs.core.account.service;

import com.yhqs.commons.consts.CacheName;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.wah.doraemon.entity.User;
import org.wah.doraemon.security.exception.LoginFailException;
import org.wah.doraemon.security.exception.TicketAuthenticationException;
import org.wah.doraemon.utils.RedisUtils;
import org.wah.ferryman.utils.AccountUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private ShardedJedisPool pool;

    @Override
    public User getByTicket(String ticket) throws Exception{
        if(StringUtils.isBlank(ticket)){
            throw new TicketAuthenticationException("无效的票据凭证");
        }

        //查询缓存
        ShardedJedis jedis = pool.getResource();

        try{
            User user = RedisUtils.get(jedis, CacheName.USER_INFO + ticket, User.class);
            if(user != null){
                //刷新有效期
                RedisUtils.expire(jedis, CacheName.USER_INFO + ticket, CacheName.LOGIN_EXPIRE);
                return user;
            }

            //查询用户信息
            user = AccountUtils.getUser(ticket);

            //缓存
            RedisUtils.save(jedis, CacheName.USER_INFO + ticket, user, CacheName.LOGIN_EXPIRE);

            return user;
        }finally{
            RedisUtils.close(jedis);
        }
    }
}
