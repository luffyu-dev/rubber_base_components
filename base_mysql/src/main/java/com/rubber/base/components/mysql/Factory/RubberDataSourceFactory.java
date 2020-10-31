package com.rubber.base.components.mysql.Factory;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.rubber.base.components.mysql.bean.RubberDataSourceBean;
import com.rubber.base.components.mysql.bean.RubberDataSourceProperties;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/10/28
 */
public class RubberDataSourceFactory {


    /**
     * 创建对象信息
     * @param rubberDataSource
     * @return
     */
    public static RubberDataSourceBean creat(RubberDataSourceProperties rubberDataSource){
        switch (rubberDataSource.getShardingType()){
            case MASTER_SLAVE:
                return createForMasterSlave(rubberDataSource);
            case CLUSTER:
                return createForCluster(rubberDataSource);
            case SINGLE:
            default:
                return createForSingle(rubberDataSource);
        }
    }



    private static RubberDataSourceBean createForMasterSlave(RubberDataSourceProperties rubberDataSource){

        RubberDataSourceBean result = new RubberDataSourceBean(rubberDataSource.getShardingType());
        //主要的节点
        DruidDataSource masterDataSource = createDataSource(rubberDataSource);
        result.addDb(rubberDataSource.getDbName(),masterDataSource);
        result.setMasterName(rubberDataSource.getDbName());
        //从节点
        List<RubberDataSourceProperties> slaves = rubberDataSource.getSlaveNode();
        if (CollUtil.isNotEmpty(slaves)){
            for (RubberDataSourceProperties nodeSlave:slaves){
                DruidDataSource dataSource = createDataSource(rubberDataSource);
                createDataSourceForFilterNull(nodeSlave,dataSource);
                result.addDb(nodeSlave.getDbName(),dataSource);
                result.addSlaveName(nodeSlave.getDbName());
            }
        }
        return result;
    }


    private static RubberDataSourceBean createForCluster(RubberDataSourceProperties rubberDataSource){
        List<RubberDataSourceProperties> clusterNode = rubberDataSource.getClusterNode();
        if (CollUtil.isEmpty(clusterNode)){
            return null;
        }
        Map<String,DruidDataSource> rubberSharding= new HashMap<>(clusterNode.size());
        for (RubberDataSourceProperties dsNode:clusterNode){
            DruidDataSource dataSource = createDataSource(rubberDataSource);
            createDataSourceForFilterNull(dsNode,dataSource);
            rubberSharding.put(dsNode.getDbName(),dataSource);
        }

        RubberDataSourceBean result = new RubberDataSourceBean(rubberDataSource.getShardingType());
        result.setShardingDbMap(rubberSharding);
        return result;
    }




    private static RubberDataSourceBean createForSingle(RubberDataSourceProperties rubberDataSource){
        DruidDataSource dataSource = createDataSource(rubberDataSource);
        RubberDataSourceBean result = new RubberDataSourceBean(rubberDataSource.getShardingType());
        result.addDb(rubberDataSource.getDbName(),dataSource);
        return result;
    }


    /**
     * 创建数据库
     */
    private static DruidDataSource createDataSource(RubberDataSourceProperties rubberDataSource){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(rubberDataSource.getUrl());
        datasource.setUsername(rubberDataSource.getUsername());
        datasource.setPassword(rubberDataSource.getPassword());
        datasource.setDriverClassName(rubberDataSource.getDriverClassName());
        //configuration
        datasource.setInitialSize(rubberDataSource.getInitialSize());
        datasource.setMinIdle(rubberDataSource.getMinIdle());
        datasource.setMaxActive(rubberDataSource.getMaxActive());
        datasource.setMaxWait(rubberDataSource.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(rubberDataSource.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(rubberDataSource.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(rubberDataSource.getValidationQuery());
        datasource.setTestWhileIdle(rubberDataSource.getTestWhileIdle());
        datasource.setTestOnBorrow(rubberDataSource.getTestOnBorrow());
        datasource.setTestOnReturn(rubberDataSource.getTestOnReturn());
        datasource.setPoolPreparedStatements(rubberDataSource.getPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(rubberDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(rubberDataSource.getFilters());
        } catch (SQLException e) {
            //log.error("druid configuration initialization filter", e);
        }
        datasource.setConnectionProperties(rubberDataSource.getConnectionProperties());
        return  datasource;
    }

    private static DruidDataSource createDataSourceForFilterNull(RubberDataSourceProperties rubberDataSource,DruidDataSource datasource){
        if (datasource == null){
            datasource = new DruidDataSource();
        }
        if (rubberDataSource.getUrl() != null){
            datasource.setUrl(rubberDataSource.getUrl());
        }
        if (rubberDataSource.getUsername() != null){
            datasource.setUsername(rubberDataSource.getUsername());
        }
        if (rubberDataSource.getPassword() != null){
            datasource.setPassword(rubberDataSource.getPassword());
        }
        if (rubberDataSource.getDriverClassName() != null){
            datasource.setDriverClassName(rubberDataSource.getDriverClassName());
        }
        if (rubberDataSource.getInitialSize() != null){
            datasource.setInitialSize(rubberDataSource.getInitialSize());
        }
        if (rubberDataSource.getMinIdle() != null){
            datasource.setMinIdle(rubberDataSource.getMinIdle());
        }
        if (rubberDataSource.getMaxActive() != null){
            datasource.setMaxActive(rubberDataSource.getMaxActive());
        }
        if (rubberDataSource.getMaxWait() != null){
            datasource.setMaxWait(rubberDataSource.getMaxWait());
        }
        if (rubberDataSource.getTimeBetweenEvictionRunsMillis() != null){
            datasource.setTimeBetweenEvictionRunsMillis(rubberDataSource.getTimeBetweenEvictionRunsMillis());
        }
        if (rubberDataSource.getMinEvictableIdleTimeMillis() != null){
            datasource.setMinEvictableIdleTimeMillis(rubberDataSource.getMinEvictableIdleTimeMillis());
        }
        if (rubberDataSource.getValidationQuery() != null){
            datasource.setValidationQuery(rubberDataSource.getValidationQuery());
        }
        if (rubberDataSource.getTestWhileIdle() != null){
            datasource.setTestWhileIdle(rubberDataSource.getTestWhileIdle());
        }
        if (rubberDataSource.getTestOnBorrow() != null){
            datasource.setTestOnBorrow(rubberDataSource.getTestOnBorrow());
        }
        if (rubberDataSource.getTestOnReturn() != null){
            datasource.setTestOnReturn(rubberDataSource.getTestOnReturn());
        }
        if (rubberDataSource.getPoolPreparedStatements() != null){
            datasource.setPoolPreparedStatements(rubberDataSource.getPoolPreparedStatements());
        }
        if (rubberDataSource.getMaxPoolPreparedStatementPerConnectionSize() != null){
            datasource.setMaxPoolPreparedStatementPerConnectionSize(rubberDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        }

        if (rubberDataSource.getFilters() != null){
            try {
                datasource.setFilters(rubberDataSource.getFilters());
            } catch (SQLException e) {
                //log.error("druid configuration initialization filter", e);
            }
        }
        if (rubberDataSource.getConnectionProperties() != null){
            datasource.setConnectionProperties(rubberDataSource.getConnectionProperties());
        }
        return  datasource;
    }




}
