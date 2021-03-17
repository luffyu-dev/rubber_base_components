package com.rubber.base.components.redis;

/**
 *
 * redis客户端的代理类
 * @author luffyu
 * Created on 2021/3/17
 */
public interface RedisClientProxy {

    String set(String key, String value);

    String get(String key);

    Boolean exists(String key);

    String type(String key);

    Long expire(String key, int seconds);

    Long pexpire(String key, long milliseconds);

    Long expireAt(String key, long unixTime);

    Long pexpireAt(String key, long millisecondsTimestamp);

    Long ttl(String key);

    Long pttl(String key);

    Long touch(String key);

    Boolean setbit(String key, long offset, boolean value);

    Boolean setbit(String key, long offset, String value);

    Boolean getbit(String key, long offset);

    Long setrange(String key, long offset, String value);

    String getrange(String key, long startOffset, long endOffset);

    String getSet(String key, String value);

    Long setnx(String key, String value);

    String setex(String key, int seconds, String value);

    String psetex(String key, long milliseconds, String value);

    Long decrBy(String key, long decrement);

    Long decr(String key);

    Long incrBy(String key, long increment);

    Double incrByFloat(String key, double increment);

    Long incr(String key);

    Long append(String key, String value);

    String substr(String key, int start, int end);

}
