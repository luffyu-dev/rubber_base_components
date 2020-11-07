package com.rubber.base.components.mysql.exception;

import com.rubber.common.utils.exception.BaseRuntimeException;

/**
 * @author luffyu
 * Created on 2020/11/1
 */
public class DefaultDBNotConnectionException extends BaseRuntimeException {


    public DefaultDBNotConnectionException() {
        super("default DataSource is not connection");
    }

    public DefaultDBNotConnectionException(String defaultName) {
        super("default DataSource ["+ defaultName +"] is not connection");
    }
}
