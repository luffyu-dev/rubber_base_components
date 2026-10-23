package com.rubber.base.components.redis.client;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.LFUCache;
import com.rubber.base.components.redis.RedisClientProxy;

/**
 * @author luffyu
 * Created on 2021/10/20
 */
public class LocalCacheClientProxy implements RedisClientProxy {


    private Cache<String, String> cache;

    public LocalCacheClientProxy() {
        this.cache = new LFUCache<>(1000);
    }


    public LocalCacheClientProxy(Cache<String, String> cache) {
        this.cache = cache;
    }


    /**
     * 单台实例的方法
     *
     * @param key
     * @param value
     * @param seconds
     */
    @Override
    @Deprecated
    public void set(String key, String value, int seconds) {
        cache.put(key, value);
    }

    @Override
    public String get(String key) {
        return cache.get(key);
    }

    @Override
    public boolean exists(String key) {
        return cache.containsKey(key);
    }

    @Override
    public long del(String... keys) {
        long count = 0L;
        for (String s : keys) {
            count += del(s);
        }
        return count;
    }

    @Override
    public long del(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
            return 1L;
        }
        return 0L;
    }

    @Override
    public long setnx(String key, String value) {
        return 0L;
    }

    @Override
    public String setex(String key, long seconds, String value) {
        return null;
    }

    @Override
    public long decrBy(String key, long decrement) {
        return 0L;
    }

    @Override
    public long decr(String key) {
        return 0L;
    }

    @Override
    public long incrBy(String key, long increment) {
        return 0L;
    }

    @Override
    public long incr(String key) {
        return 0L;
    }
}
