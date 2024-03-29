package com.rubber.base.components.redis.client;

import com.rubber.base.components.redis.RedisClientProxy;

/**
 * @author luffyu
 * Created on 2021/3/20
 */
public class NoCacheClientProxy implements RedisClientProxy {

    /**
     * 单台实例的方法
     *
     * @param key
     * @param value
     * @param seconds
     */
    @Override
    public void set(String key, String value, int seconds) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public Boolean exists(String key) {
        return null;
    }

    @Override
    public Long del(String... keys) {
        return null;
    }

    @Override
    public Long del(String key) {
        return null;
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
