package com.rubber.base.components.util.result.bean;


import lombok.Data;

import java.util.Date;

@Data
public class RubberBaseReq {

    /**
     * 请求Id
     */
    private String traceId;


    /**
     * 系统请求时间
     */
    private Date sysReqTime = new Date();



    /**
     * app的版本信息
     */
    private String appVersion;



    /**
     * 页面的版本
     */
    private Integer v = 1;
}
