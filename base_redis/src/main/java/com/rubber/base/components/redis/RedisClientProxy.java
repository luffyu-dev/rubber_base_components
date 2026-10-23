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
    void set(String key, String value, int seconds);

    String get(String key);

    /**
     * 注：Jedis 5+ 起 exists/incr/del/setnx 等返回基本类型 boolean/long，
     * 接口同步调整为基本类型，避免与 Jedis 子类方法签名冲突。
     */
    boolean exists(String key);

    long del(String... keys);

    long del(String key);

    long setnx(String key, String value);

    String setex(String key, long seconds, String value);

    long decrBy(String key, long decrement);

    long decr(String key);

    long incrBy(String key, long increment);

    long incr(String key);

}
