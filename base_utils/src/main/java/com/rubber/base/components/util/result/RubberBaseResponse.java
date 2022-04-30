package com.rubber.base.components.util.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author luffyu
 * Created on 2022/4/21
 */@Data
public class RubberBaseResponse implements Serializable {

    /**
     * code信息
     */
    private String code;

    /**
     * msg信息
     */
    private String msg;


    /**
     * 登录session相关参数
     */
    private RubberSession session;

    /**
     * 系统登录的相关参数
     */
    private RubberSystem system;


    /**
     * 数据信息
     */
    private Object data;


}
