package com.rubber.base.components.config.exception;


import com.rubber.base.components.util.exception.BaseRuntimeException;

/**
 * @author luffyu
 * Created on 2020/12/13
 */
public class RubberConfigException extends BaseRuntimeException {

    public RubberConfigException() {
    }

    public RubberConfigException(String message) {
        super(message);
    }

    public RubberConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public RubberConfigException(Throwable cause) {
        super(cause);
    }

    public RubberConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
