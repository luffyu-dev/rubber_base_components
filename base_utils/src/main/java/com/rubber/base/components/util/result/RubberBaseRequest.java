package com.rubber.base.components.util.result;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2022/4/21
 */
@Data
public class RubberBaseRequest {

    /**
     * 登录session相关参数
     */
    private RubberSession session;

    /**
     * 系统登录的相关参数
     */
    private RubberSystem system;


}
