package com.allen.bargains_for_seconds.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();   //返回到连接池中
        }
    }

    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = jedis.get(prefix.getPrefix() + key);
            T t = stringToBean(str, clazz);
            return t;
        } finally {
            returnToPool(jedis);
        }

    }

    public <T> Boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String str = beanToString(value);

            if(str == null || str.length() <= 0) {
                return false;
            }

            // 判断过期时间
            int seconds = prefix.expireSeconds();
            if (seconds <= 0) {
                jedis.set(prefix.getPrefix() + key, str);
            } else {
                jedis.setex(prefix.getPrefix() + key, seconds, str);
            }

            return true;
        } finally {
            returnToPool(jedis);
        }

    }

    private <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return ""+value;
        } else if (clazz == String.class) {
            return (String)value;
        } else if (clazz == long.class || clazz == Long.class) {
            return ""+value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    private <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T)Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T)str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T)Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }

    }

    public <T> boolean exists(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = prefix.getPrefix() + key;

            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


}
