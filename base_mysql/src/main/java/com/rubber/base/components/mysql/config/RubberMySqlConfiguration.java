package com.rubber.base.components.mysql.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.rubber.base.components.mysql.config.properties.RubberProxyConfigProperties;
import com.rubber.base.components.mysql.plugins.sharding.config.RubberShardingConfig;
import com.rubber.base.components.mysql.config.properties.bean.DruidDatasourceProperties;
import com.rubber.base.components.mysql.exception.NotFoundDataSourceException;
import com.rubber.base.components.mysql.interceptor.RubberShardingMybatisInterceptor;
import com.rubber.base.components.mysql.plugins.MyRubberDruidDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author luffyu
 * Created on 2022/10/14
 */
@Data
@Slf4j
@Configuration
public class RubberMySqlConfiguration {

    @Autowired(required = false)
    private RubberShardingConfig rubberShardingConfig;


    @Primary
    @Bean(name = "dataSource")
    public DataSource createDataSource(RubberProxyConfigProperties properties){
        Map<String,MyRubberDruidDataSource> dataSourceMap = new HashMap<>();
        if (MapUtil.isEmpty(properties.getDbInstances())){
            throw new NotFoundDataSourceException();
        }
        MyRubberDruidDataSource defaultDataSource = null;
        for (String key:properties.getDbInstances().keySet()){
            DruidDatasourceProperties rubberMysqlProperties = properties.getDbInstances().get(key);
            MyRubberDruidDataSource druidDataSource = createDruidDataSource(rubberMysqlProperties);
            dataSourceMap.put(key,druidDataSource);
            if (defaultDataSource == null){
                defaultDataSource = druidDataSource;
            }
        }
        if (defaultDataSource != null){
            defaultDataSource.setDataSourceMap(dataSourceMap);
        }
        return defaultDataSource;
    }


    @Bean
    public SqlSessionTemplate sqlSessionTemplate(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sql = new MybatisSqlSessionFactoryBean();
        sql.setDataSource(dataSource);

        List<Interceptor> listInterceptor = new ArrayList<>();
        // mybatis-plus的插件
        MybatisPlusInterceptor mybatisPlusInterceptor =  new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());

        listInterceptor.add(mybatisPlusInterceptor);

        if (rubberShardingConfig != null){
            // mybait-的分库分表插件
            RubberShardingMybatisInterceptor shardingMybatisInterceptor = new RubberShardingMybatisInterceptor(rubberShardingConfig);
            listInterceptor.add(shardingMybatisInterceptor);
        }
        sql.setPlugins(ArrayUtil.toArray(listInterceptor,Interceptor.class));
        return new SqlSessionTemplate(sql.getObject());

    }




    protected MyRubberDruidDataSource createDruidDataSource(DruidDatasourceProperties mysqlProperties){
        MyRubberDruidDataSource datasource = new MyRubberDruidDataSource();
        datasource.setUrl(mysqlProperties.getUrl());
        datasource.setUsername(mysqlProperties.getUsername());
        datasource.setPassword(mysqlProperties.getPassword());
        datasource.setDriverClassName(mysqlProperties.getDriverClassName());

        datasource.setInitialSize(mysqlProperties.getInitialSize());
        datasource.setMinIdle(mysqlProperties.getMinIdle());
        datasource.setMaxActive(mysqlProperties.getMaxActive());
        datasource.setMaxWait(mysqlProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(mysqlProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(mysqlProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(mysqlProperties.getValidationQuery());
        datasource.setTestWhileIdle(mysqlProperties.isTestWhileIdle());
        datasource.setTestOnBorrow(mysqlProperties.isTestOnBorrow());
        datasource.setTestOnReturn(mysqlProperties.isTestOnReturn());
        datasource.setPoolPreparedStatements(mysqlProperties.isPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(mysqlProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(mysqlProperties.getFilters());
        } catch (SQLException e) {
        }
        datasource.setConnectionProperties(mysqlProperties.getConnectionProperties());

        if (mysqlProperties.isOpenUtf8mb4()){
            List<Object> list = new ArrayList<>();
            list.add("set names utf8mb4");
            datasource.setConnectionInitSqls(list);
        }
        return  datasource;
    }


}
