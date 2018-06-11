package com.yhqs.commons.security.config.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.core.io.support.ResourcePatternResolver;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Configuration
@PropertySource("classpath:redis.properties")
@Setter
@Getter
public class RedisConfig{

    @Value("${redis.pool.maxIdle}")
    private Integer maxIdle;

    @Value("${redis.pool.maxTotal}")
    private Integer maxTotal;

    @Value("${redis.pool.testOnBorrow}")
    private Boolean testOnBorrow;

    @Value("${redis.pool.testOnReturn}")
    private Boolean testOnReturn;

    @Bean
    public JedisPoolConfig poolConfig(){
        JedisPoolConfig config = new JedisPoolConfig();

        //配置
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        return config;
    }

    @Bean
    public ShardedJedisPool jedisPool(){
        List<JedisShardInfo> infos = new ArrayList<JedisShardInfo>();

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource("classpath:/redis.properties");

        try{
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);

            //键集合
            Set<Object> keys = properties.keySet();
            for(Object key : keys){
                if(((String) key).startsWith("redis.uri.")){
                    JedisShardInfo jedisShardInfo = new JedisShardInfo(properties.get(key).toString());
                    infos.add(jedisShardInfo);
                }
            }

            return new ShardedJedisPool(poolConfig(), infos);
        }catch(Exception e){
            throw new RuntimeException("无法加载配置文件");
        }
    }
}
