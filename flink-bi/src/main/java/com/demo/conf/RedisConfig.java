package com.demo.conf;

import com.demo.util.ConfigUtils;

public class RedisConfig {
    /**
     * 同程redis cache-name
     */
    public static final String REDIS_CACHE_NAME= ConfigUtils.getPros("redis.cache.name");
    //方案两小时内被点击redis保存时间
    public static final Long REDIS_PLAN_CLICK_STATS_ALIVE=3600*1L;
    //用户最近点击过方案,10分钟
    public static final Long REDIS_USER_LATEST_CLICK_ALIVE=60*10L;
}
