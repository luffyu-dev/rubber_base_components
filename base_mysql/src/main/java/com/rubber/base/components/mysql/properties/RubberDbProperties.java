package com.rubber.base.components.mysql.properties;

import com.rubber.base.components.mysql.bean.DBClusterType;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
public class RubberDbProperties {


    /**
     * db的名称，用于标示一个唯一的Db信息
     */
    private String name;

    /**
     * db的类型
     * @see DBClusterType
     */
    private DBClusterType type;


    /**
     * 分表key值信息
     */
    private String shardKey;

    /**
     * 多种配置
     */
    private Map<String, DbConnectProperties> connects;


    /**
     * 配置信息
     */
    private DbConfigProperties config;


}