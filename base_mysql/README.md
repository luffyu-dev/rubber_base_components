### MySQl配置模版

##### 一：DB配置说明

1. DB数据库参数说明
、、、
server:
  port: 8083

rubber:
  proxy:
    db: ## db配置
      userDB: #数据库的名称
        setDbName: userDB #数据库的名称
        type: SINGLE  #数据库的类型，单例 或者或者集群 @see DBClusterType 两种模式都支持主从
        config: # 数据库的配置
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
        connects: #连接配置，支持多个master配置，当 type: SINGLE  的时候 读取第一个配置
          - url: jdbc:mysql://127.0.0.1:3306/user_db_00?useUnicode=true;characterEncoding=utf-8
            username: root
            password: root
            driverClassName: com.mysql.cj.jdbc.Driver
            dbName: userDb
            slaves: # 从库配置 所有的实例都可以设置多个从库配置
              - url: jdbc:mysql://127.0.0.1:3306/user_db_01?useUnicode=true;characterEncoding=utf-8
                username: root
                password: root
                driverClassName: com.mysql.cj.jdbc.Driver
      USER_DB_CLUSTER: ## 实例2
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
        connects: #连接配置，支持多个master配置，当 type: CLUSTER  的时候 ，支持多个配置
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
、、、


2. DB数据配置说明
   - 方式1：直接将上面的配置 写在 application.yml 中
   - 方式2：通过nacos拉取
    + 设置 在bootstrap中设置 rubber.proxy.config.dbSet ，例如为 userDB
    + base_config ，会自动按照 ${}+-rubber-config-mysql-${rubber.proxy.config.dbSet}.yml 的文件地址拉去配置文件
 
 
 ##### 二、table配置说明
 
 1. 数据配置
、、、
rubber:
  proxy:
    table-config:
      - logicTableName: user_order #逻辑库表的名称
        ruleType: SINGLE_DB_TEN_TABLE_HASH #分表的方式 具体@see RuleType
        dbName: userDb #当前表所属的数据库名称
        shardingColumn: id 当前分表的id
        tableNum: 1 ##最大的表数字，最后会按照 table_${0..tableNum-1}的方式来生成表,最大支持的值为10
、、、