package com.trs.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * Created by zhangheng on 2016/10/26.
 */
@Component
public class JedisDao {

    @Autowired
    private JedisPool jedisPool;

    public String hmset(byte[] key, Map<byte[],byte[]> hash){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hmset(key, hash);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedisPool.returnResourceObject(jedis);
        }
    }

    public Long hdel(byte[] key, byte[]...field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hdel(key, field);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedisPool.returnResourceObject(jedis);
        }
    }

    public void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedisPool.returnResourceObject(jedis);
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.hget(key, field);
        } catch (Exception e) {
            throw e;
        } finally {
            if (jedis != null)
                jedisPool.returnResourceObject(jedis);
        }
    }
}
