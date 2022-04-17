package com.rubber.base.components.util.result;

/**
 * @author luffyu
 * Created on 2019-07-12
 */
public interface IResultHandle {

    /**
     * 错误的code
     * @return 返回code值
     */
    String getCode();

    /**
     * 错误的msg
     * @return 返回msg信息
     */
    String getMsg();

    /**
     * 返回业务数据
     * @return 返回数据信息
     */
    Object getData();


    /**
     * 网关层数据
     * @return 返回网关数据
     */
    Object getSysData();



}
