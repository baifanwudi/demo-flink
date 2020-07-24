package com.demo.conf;


import com.demo.util.ConfigUtils;

public class RedisConfig {
    /**
     * 同程redis cache-name
     */
    public static final String REDIS_CACHE_NAME= ConfigUtils.getPros("redis.cache.name");
    /**
     * key保持的时间
     */
    public static final Long REDIS_KEY_ALIVE=Long.parseLong(ConfigUtils.getPros("redis.cache.period"));
}
