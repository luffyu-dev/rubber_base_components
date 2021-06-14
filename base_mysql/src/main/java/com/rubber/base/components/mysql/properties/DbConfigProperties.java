package com.rubber.base.components.mysql.properties;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
@Data
public class DbConfigProperties{

    /**
     * #初始化的大小
     */
    private Integer initialSize = 5;

    /**
     * 最小连接数 也就是 minPoolSize
     */
    private Integer minIdle = 5;

    /**
     * 最大连接数 也就是 maxPoolSize
     */
    private Integer maxActive = 5;

    /**
     * 获取连接等待的超时时间
     */
    private Integer maxWait = 50;

    /**
     * 间隔多久进行一次关闭空闲连接操作 单位是毫秒
     */
    private Integer timeBetweenEvictionRunsMillis = 60000;

    /**
     * 一个连接在连接池中的最小生存时间 单位是毫秒
     */
    private Integer minEvictableIdleTimeMillis = 300000;

    /**
     * #自动测试连接查询
     * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
     *  SELECT 1 FROM DUAL
     */
    private String validationQuery = "SELECT 1 FROM DUAL";

    private Boolean logSlowSql = false;
    private Boolean testWhileIdle = true;
    private Boolean testOnBorrow = false;
    private Boolean testOnReturn = false;
    /**
     * 并且指定每个连接上PSCache的大小
     */
    private Boolean poolPreparedStatements = true;
    private Integer maxPoolPreparedStatementPerConnectionSize = 20;

    /**
     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙 防御sql注入
     */
    private String filters = "stat,wall,log4j";

    /**
     * 通过connectProperties属性来打开mergeSql功能；慢SQL记录
     */
    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";

}
