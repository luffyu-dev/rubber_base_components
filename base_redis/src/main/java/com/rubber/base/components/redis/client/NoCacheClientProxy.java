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
    public boolean exists(String key) {
        return false;
    }

    @Override
    public long del(String... keys) {
        return 0L;
    }

    @Override
    public long del(String key) {
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
