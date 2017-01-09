package com.trs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by zhangheng on 2016/10/26.
 */
@Configuration
@EnableCaching
@PropertySource(value = "classpath:/redis.properties")
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.pool.max-active}")
    private Integer maxActives;

    @Value("${spring.redis.pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.redis.pool.min-idle}")
    private Integer minIdle;

    @Value("${spring.redis.pool.max-wait}")
    private Integer maxWaitMillis;

    @Value("${spring.redis.uri}")
    private String uri;


    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        URI redisUri = null;
        try {
            redisUri = new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, redisUri);
        return jedisPool;
    }
}
