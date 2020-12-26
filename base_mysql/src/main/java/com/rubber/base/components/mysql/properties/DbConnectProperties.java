package com.rubber.base.components.mysql.properties;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
@Data
public class DbConnectProperties {

    /**
     * 连接的必要参数
     */
    private String url;
    private String username;
    private String password;
    private String driverClassName;

}
