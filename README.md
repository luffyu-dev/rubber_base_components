# rubber_base_components
一个包含了rubber分布式框架的基础组件服务，包含mysql，redis，mq等常用中间件的配置，在项目中依赖该组件之后，至需要简单设置一个服务的名称，即可完成配置装配

## DB 数据源配置

1. mysql的配置对象类为 com.rubber.base.components.mysql.properties.RubberDbProperties

2. 结构说明
- setDbName: 实例名称
- type: 集群类型
- shardKey: 分发key值  待开发
- connects: 连接名称
- config: 数据库配置信息


实例：
```
rubber:
  proxy:
    dbSetName: USER_DB_CLUSTER
    db:
      USER_DB_SINGLE:
        setDbName: USER_DB
        type: SINGLE
        shardKey: uid
        config:
          type: com.alibaba.druid.pool.DruidDataSource
          platform: mysql
          initialSize: 5
          minIdle: 5
          maxIdle: 5
          maxActive: 50
          maxWait: 60000
          timeBetweenEvictionRunsMillis: 60000
          minEvictableIdleTimeMillis: 300000
          validationQuery: SELECT 1 FROM DUAL
          testWhileIdle: true
          testOnBorrow: false
          testOnReturn: false
          poolPreparedStatements: true
          maxPoolPreparedStatementPerConnectionSize: 20
          # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
          filters: stat,wall,log4j
          # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
          connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
        connects:
          - url: jdbc:mysql://127.0.0.1:3306/user_db_00?useUnicode=true;characterEncoding=utf-8
            username: root
            password: root
            driverClassName: com.mysql.cj.jdbc.Driver
            dbName: userDb
            slaves:
              - url: jdbc:mysql://127.0.0.1:3306/user_db_01?useUnicode=true;characterEncoding=utf-8
                username: root
                password: root
                driverClassName: com.mysql.cj.jdbc.Driver
      USER_DB_CLUSTER:
        setDbName: USER_DB
        type: CLUSTER
        shardKey: uid
        config:
          type: com.alibaba.druid.pool.DruidDataSource
          platform: mysql
          initialSize: 5
          minIdle: 5
          maxIdle: 5
          maxActive: 50
          maxWait: 60000
          timeBetweenEvictionRunsMillis: 60000
          minEvictableIdleTimeMillis: 300000
          validationQuery: SELECT 1 FROM DUAL
          testWhileIdle: true
          testOnBorrow: false
          testOnReturn: false
          poolPreparedStatements: true
          maxPoolPreparedStatementPerConnectionSize: 20
          # 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
          filters: stat,wall,log4j
          # 通过connectProperties属性来打开mergeSql功能；慢SQL记录
          connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
          driver-class-name: com.mysql.cj.jdbc.Driver
        connects:
          - url: jdbc:mysql://127.0.0.1:3306/user_db_00?useUnicode=true;characterEncoding=utf-8
            username: root
            password: root
            driverClassName: com.mysql.cj.jdbc.Driver
            dbName: userDb00
            slaves:
              - url: jdbc:mysql://127.0.0.1:3306/user_db_01?useUnicode=true;characterEncoding=utf-8
                username: root
                password: root
                driverClassName: com.mysql.cj.jdbc.Driver
          - url: jdbc:mysql://127.0.0.1:3306/user_db_01?useUnicode=true;characterEncoding=utf-8
            username: root
            password: root
            driverClassName: com.mysql.cj.jdbc.Driver
            dbName: userDb01

```