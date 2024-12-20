package com.rubber.base.components.util.session;

import lombok.Data;

import java.util.Date;

/**
 * @author luffyu
 * Created on 2022/8/16
 */
@Data
public class BaseUserSession {


    /**
     * 页面的版本
     */
    private Integer v = 1;

    /**
     * 当前用户的uid
     */
    private Integer uid;

    /**
     * 当前用户的名称
     */
    private String name;

    /**
     * 系统请求时间
     */
    private Date sysReqTime;


    /**
     * app的版本信息
     */
    private String appVersion;

}
