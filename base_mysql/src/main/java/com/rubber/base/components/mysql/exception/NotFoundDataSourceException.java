package com.rubber.base.components.mysql.exception;

import com.rubber.common.utils.exception.BaseRuntimeException;

/**
 * @author luffyu
 * Created on 2020/11/1
 */
public class NotFoundDataSourceException extends BaseRuntimeException {


    public NotFoundDataSourceException() {
        super("DataSource is null");
    }

    public NotFoundDataSourceException(String message) {
        super(message);
    }
}
