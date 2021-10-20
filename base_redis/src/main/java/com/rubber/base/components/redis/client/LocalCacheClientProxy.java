package com.rubber.base.components.redis.client;

import cn.hutool.cache.Cache;
import cn.hutool.cache.impl.LFUCache;
import com.rubber.base.components.redis.RedisClientProxy;

/**
 * @author luffyu
 * Created on 2021/10/20
 */
public class LocalCacheClientProxy implements RedisClientProxy {


    private Cache<String,String> cache;

    private LocalCacheClientProxy(){
        this.cache = new LFUCache<>(1000);
    }


    private LocalCacheClientProxy(Cache<String,String> cache){
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
        cache.put(key,value);
    }

    @Override
    public String get(String key) {
        return cache.get(key);
    }

    @Override
    public Boolean exists(String key) {
        return cache.containsKey(key);
    }

    @Override
    public Long del(String... keys) {
        Long l1 = 0L;
        for (String s:keys){
            l1 += del(s);
        }
        return l1;
    }

    @Override
    public Long del(String key) {
        if (cache.containsKey(key)) {
            cache.remove(key);
            return 1L;
        }
        return 0L;
    }

    @Override
    public Long setnx(String key, String value) {
        return null;
    }

    @Override
    public String setex(String key, int seconds, String value) {
        return null;
    }

    @Override
    public Long decrBy(String key, long decrement) {
        return null;
    }

    @Override
    public Long decr(String key) {
        return null;
    }

    @Override
    public Long incrBy(String key, long increment) {
        return null;
    }

    @Override
    public Long incr(String key) {
        return null;
    }
}
