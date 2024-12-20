package com.rubber.base.components.mysql.config.properties.bean;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2022/10/15
 */
@Data
public class DruidDatasourceProperties {

    private String driverClassName;
    private String url;
    private String username;
    private String password;


    /**
     * #初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
     */
    private int initialSize = 1;
    /**
     * 最小连接池数量
     */
    private int minIdle = 1;
    /**
     * 最大连接池数量
     */
    private int maxActive = 100;
    /**
     * 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。建议配置2000ms
     */
    private long maxWait = 2000;
    /**
     * Destroy线程定时监测的间隔， Destroy线程会检测连接的间隔时间，如果连接空闲时间大于等于minEvictableIdleTimeMillis则关闭物理连接。2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
     */
    private long timeBetweenEvictionRunsMillis = 60000;
    /**
     * 连接保持空闲而不被驱逐的最长时间。建议值：5* timeBetweenEvictionRunsMillis
     */
    private long minEvictableIdleTimeMillis = 300000;
    /**
     * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null
     */
    private String validationQuery = "SELECT 1";
    /**
     * 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。
     */
    private boolean testWhileIdle = true;
    /**
     * 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。建议值：false
     */
    private boolean testOnBorrow = false;
    /**
     *
     */
    private boolean testOnReturn = false;
    /**
     * #是否缓存preparedStatement，也就是PSCache。PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql5.5以下的版本中没有PSCache功能，建议关闭掉。5.5及以上版本有PSCache，建议开启。
     */
    private boolean poolPreparedStatements = true;
    /**
     * #要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
     */
    private int maxPoolPreparedStatementPerConnectionSize;

    /**
     * #属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有：监控统计用的filter:stat，日志用的filter:log4j， 防御sql注入的filter:wall
     */
    private String filters = "stat,wall,log4j2";

    /**
     * #建立新连接时将发送到JDBC驱动程序的连接属性。字符串的格式必须为[propertyName = property;] *注 - “用户”和“密码”属性将被明确传递，因此不需要在此处包含
     */
    private String connectionProperties = "druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000";


    /**
     * 是否支持表情
     */
    private boolean openUtf8mb4 = false;
}
