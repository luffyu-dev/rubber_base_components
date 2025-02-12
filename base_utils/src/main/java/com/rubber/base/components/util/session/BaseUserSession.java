package com.rubber.base.components.util.session;

import com.rubber.base.components.util.result.bean.RubberBaseReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @author luffyu
 * Created on 2022/8/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseUserSession extends RubberBaseReq {



    /**
     * 当前用户的uid
     */
    private Integer uid;

    /**
     * 当前用户的名称
     */
    private String name;


    /**
     * 用户角色
     */
    private String role;


}
