package com.rubber.base.components.redis.exception;

import com.rubber.common.utils.exception.BaseRuntimeException;

/**
 * redis的实例无法找到的异常
 * @author luffyu
 * Created on 2021/3/17
 */
public class RedisInstanceNotFoundException extends BaseRuntimeException {

    public RedisInstanceNotFoundException(String message) {
        super(message);
    }
}
