package com.rubber.base.components.util.result;

import lombok.Data;
import java.io.Serializable;

/**
 * @author luffyu
 * Created on 2022/4/21
 */
@Data
public class RubberSystem implements Serializable {

    /**
     * 链路id
     */
    private String traceId;


    /**
     * 系统时间
     */
    private long resTime = System.currentTimeMillis();


    /**
     * 当前请求的ip
     */
    private String ip;


}
