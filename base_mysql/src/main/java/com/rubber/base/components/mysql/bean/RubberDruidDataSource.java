package com.rubber.base.components.mysql.bean;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 *
 * 当前数据库的数据分片配置表
 * @author luffyu
 * Created on 2020/10/25
 */
@Data
public class RubberDruidDataSource extends DruidDataSource {

    /**
     * 配置所属于的环境
     */
    private String env;

    /**
     * 所属集群
     */
    private String dbClusterName;

    /**
     * 数据库的名称
     */
    private String dbName;

    /**
     * 是否是主库
     */
    private boolean isMaster;


    /**
     * 主库的配置
     */
    private RubberDruidDataSource master;


    /**
     * 从库的配置
     */
    private Map<String,RubberDruidDataSource> slaves;


}
