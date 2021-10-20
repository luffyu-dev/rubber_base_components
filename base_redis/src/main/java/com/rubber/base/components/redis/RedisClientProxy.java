package com.rubber.base.components.redis;



/**
 *
 * redis客户端的代理类
 * @author luffyu
 * Created on 2021/3/17
 */
public interface RedisClientProxy {

    /**
     * 单台实例的方法
     */
    void set(String key, String value,int seconds);

    String get(String key);

    Boolean exists(String key);

    Long del(String... keys);

    Long del(String key);

    Long setnx(String key, String value);

    String setex(String key, int seconds, String value);

    Long decrBy(String key, long decrement);

    Long decr(String key);

    Long incrBy(String key, long increment);

    Long incr(String key);

}
