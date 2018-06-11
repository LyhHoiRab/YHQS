package com.yhqs.core.init;

import com.yhqs.commons.consts.CacheName;
import com.yhqs.core.permission.dao.FunctionDao;
import com.yhqs.core.permission.entity.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wah.doraemon.utils.RedisUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class CacheInit{

    @Autowired
    private FunctionDao functionDao;

    @Autowired
    private ShardedJedisPool pool;

    @PostConstruct
    public void init(){
        List<Function> functions = functionDao.find(null, null, false, null);

        if(functions != null && !functions.isEmpty()){
            ShardedJedis jedis = pool.getResource();
            RedisUtils.save(jedis, CacheName.NO_PERMISSION_URL, functions);
            RedisUtils.close(jedis);
        }
    }
}
