package com.rubber.base.components.mysql.exception;


import com.rubber.base.components.util.exception.BaseRuntimeException;

/**
 * @author luffyu
 * Created on 2020/11/1
 */
public class NotSingleDataSourceException extends BaseRuntimeException {


    public NotSingleDataSourceException() {
        super("single db but found two connection");
    }

    public NotSingleDataSourceException(String message) {
        super(message);
    }
}
