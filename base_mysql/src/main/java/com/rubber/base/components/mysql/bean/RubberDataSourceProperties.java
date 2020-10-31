package com.rubber.base.components.mysql.bean;

import lombok.Data;

import java.util.List;

/**
 *
 * @author luffyu
 * Created on 2020/10/28
 */
@Data
public class RubberDataSourceProperties {

    /**
     * 当前库的名称
     */
    private String dbName;


    /**
     * 数据库的分库规则
     * @see DBShardingType
     */
    private DBShardingType shardingType = DBShardingType.SINGLE;


    /**
     * 主从模式中的从节点
     */
    private List<RubberDataSourceProperties> slaveNode;

    /**
     * 集群节点
     */
    private List<RubberDataSourceProperties> clusterNode;


    /**
     * 需要分表的table信息
     */
    private List<RubberTableProperties> tables;

    /**
     * 连接的必要参数
     */
    private String url;
    private String username;
    private String password;
    private String driverClassName;

    /**
     * #初始化的大小
     */
    private Integer initialSize;

    /**
     * 最小连接数 也就是 minPoolSize
     */
    private Integer minIdle;

    /**
     * 最大连接数 也就是 maxPoolSize
     */
    private Integer maxActive;

    /**
     * 获取连接等待的超时时间
     */
    private Integer maxWait;

    /**
     * 间隔多久进行一次关闭空闲连接操作 单位是毫秒
     */
    private Integer timeBetweenEvictionRunsMillis;

    /**
     * 一个连接在连接池中的最小生存时间 单位是毫秒
     */
    private Integer minEvictableIdleTimeMillis;

    /**
     * #自动测试连接查询
     * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
     *  SELECT 1 FROM DUAL
     */
    private String validationQuery;

    private Boolean logSlowSql;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    /**
     * 并且指定每个连接上PSCache的大小
     */
    private Boolean poolPreparedStatements;
    private Integer maxPoolPreparedStatementPerConnectionSize;

    /**
     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙 防御sql注入
     */
    private String filters;

    /**
     * 通过connectProperties属性来打开mergeSql功能；慢SQL记录
     */
    private String connectionProperties;


}
