package com.rubber.base.components.util.result.exception;


import com.rubber.base.components.util.result.IResultHandle;

/**
 * @author luffyu
 * Created on 2019-07-12
 *
 * 需要返回定义的数据类型的异常
 */
public interface IResultException {

    /**
     * 返回结果的handler
     * @return  返回结果集合
     */
    IResultHandle getResult();
}
