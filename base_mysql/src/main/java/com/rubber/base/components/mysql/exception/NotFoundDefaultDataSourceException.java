package com.rubber.base.components.mysql.exception;

import com.rubber.common.utils.exception.BaseRuntimeException;

/**
 * @author luffyu
 * Created on 2020/11/1
 */
public class NotFoundDefaultDataSourceException extends BaseRuntimeException {


    public NotFoundDefaultDataSourceException() {
        super("default DataSource is null");
    }

    public NotFoundDefaultDataSourceException(String message) {
        super(message);
    }
}
