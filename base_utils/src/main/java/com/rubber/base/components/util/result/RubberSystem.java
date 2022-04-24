package com.rubber.base.components.util.result;

import lombok.Data;


/**
 * @author luffyu
 * Created on 2022/4/21
 */
@Data
public class RubberSystem {

    /**
     * 链路id
     */
    private String traceId;

    /**
     * 系统时间
     */
    private long requestTime;

    /**
     * 请求时间
     */
    private long responseTime;


    /**
     * 当前请求的ip
     */
    private String ip;


}
