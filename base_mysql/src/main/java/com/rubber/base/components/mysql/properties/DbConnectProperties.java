package com.rubber.base.components.mysql.properties;

import lombok.Data;

import java.util.List;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
@Data
public class DbConnectProperties {
    /**
     * 数据库的配置名称
     */
    private String dbName;

    /**
     * 连接的必要参数
     */
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    /**
     * 从节点配置
     */
    private List<DbConnectProperties> slaves;

}
