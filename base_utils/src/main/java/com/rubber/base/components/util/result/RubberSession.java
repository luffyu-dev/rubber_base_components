package com.rubber.base.components.util.result;

import lombok.Data;

/**
 * session的相关信息
 * @author luffyu
 * Created on 2022/4/21
 *
 */
@Data
public class RubberSession {


    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 操作人
     */
    private String operator;



}
