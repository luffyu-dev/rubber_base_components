package com.rubber.base.components.mysql.exception;

import com.rubber.common.utils.exception.BaseRuntimeException;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class TableRuleDbNotFoundException extends BaseRuntimeException {

    public TableRuleDbNotFoundException(String message) {
        super(message);
    }
}
