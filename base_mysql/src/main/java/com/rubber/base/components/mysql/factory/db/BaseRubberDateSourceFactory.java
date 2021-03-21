package com.rubber.base.components.mysql.factory.db;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.rubber.base.components.mysql.bean.RubberDruidDataSource;
import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.constant.RubberDbConstant;
import com.rubber.base.components.mysql.exception.NotFoundDataSourceException;
import com.rubber.base.components.mysql.properties.DbConfigProperties;
import com.rubber.base.components.mysql.properties.DbConnectProperties;
import com.rubber.base.components.mysql.properties.RubberDbProperties;
import io.shardingsphere.api.algorithm.masterslave.RandomMasterSlaveLoadBalanceAlgorithm;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
public abstract class BaseRubberDateSourceFactory implements RubberDataSourceFactory {



    @Override
    public RubberDruidDataSource createDbSource(RubberDbProperties rubberDbProperties){
        checkDbProperties(rubberDbProperties);

        List<DbConnectProperties> clusters = rubberDbProperties.getConnects();
        DbConfigProperties config = rubberDbProperties.getConfig();
        RubberDruidDataSource druidDataSource = new RubberDruidDataSource();
        druidDataSource.setSetDbName(rubberDbProperties.getSetDbName());
        druidDataSource.setShardingType(rubberDbProperties.getType());

        Map<String,DruidDataSource> clusterMap = new HashMap<>(clusters.size());
        Map<String,List<DruidDataSource>> slavesMap = new HashMap<>(2);

        for (DbConnectProperties cluster:clusters){
            DruidDataSource masterDataSource = createDruidDataSource(cluster, config);
            clusterMap.put(cluster.getDbName(),masterDataSource);
            Map<String, List<DruidDataSource>> slaveListMap = doCreateSlaveDateSource(cluster.getDbName(), cluster.getSlaves(), config);
            if (MapUtil.isNotEmpty(slaveListMap)){
                slavesMap.putAll(slaveListMap);
            }
        }
        druidDataSource.setClusters(clusterMap);
        druidDataSource.setSlaves(slavesMap);
        return druidDataSource;

    }


    @Override
    public RubberShardingRuleBean createDbRule(RubberDbProperties rubberDbProperties) {
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();

        //创建db规则
        Map<String, DataSource> shardingDbMap = doCreatDbRule(rubberDbProperties,shardingRuleConfiguration);

        return new RubberShardingRuleBean(shardingDbMap,shardingRuleConfiguration);
    }



    /**
     * 创建Db的配置规则
     * @param rubberDbProperties db配置信息
     * @param shardingRuleConfiguration 分表规则信息
     * @return
     */
    protected Map<String, DataSource> doCreatDbRule(RubberDbProperties rubberDbProperties,ShardingRuleConfiguration shardingRuleConfiguration){
        RubberDruidDataSource rubberDruidDataSource = createDbSource(rubberDbProperties);

        Map<String, DataSource> shardingDbMap = new HashMap<>(16);
        for (Map.Entry<String,DruidDataSource> dataSourceEntry:rubberDruidDataSource.getClusters().entrySet()){
            String dbName = dataSourceEntry.getKey();
            shardingDbMap.put(dbName,dataSourceEntry.getValue());

            if (MapUtil.isNotEmpty(rubberDruidDataSource.getSlaves())){
                List<DruidDataSource> slaveDataSources = rubberDruidDataSource.getSlaves().get(dbName);
                MasterSlaveRuleConfiguration masterSlaveRule = doCreateMasterSlaveRule(dbName, slaveDataSources, shardingDbMap);
                if (masterSlaveRule != null){
                    shardingRuleConfiguration.getMasterSlaveRuleConfigs().add(masterSlaveRule);
                }
            }

        }
        return shardingDbMap;
    }


    /**
     * 检测配置
     */
    protected void checkDbProperties(RubberDbProperties rubberDbProperties){
        if (CollUtil.isEmpty(rubberDbProperties.getConnects())){
            throw new NotFoundDataSourceException();
        }
    }


    /**
     * 创建主从配置
     * @param masterName master的名称
     * @param slaveList slave的集合
     * @param shardingDbMap 需要sharding的Map数据源
     * @return 返回主从配置
     */
    private MasterSlaveRuleConfiguration doCreateMasterSlaveRule(String masterName, List<DruidDataSource> slaveList, Map<String, DataSource> shardingDbMap){
        if (CollUtil.isEmpty(slaveList)){
            return null;
        }
        MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = new MasterSlaveRuleConfiguration();
        masterSlaveRuleConfiguration.setName(masterName);
        masterSlaveRuleConfiguration.setMasterDataSourceName(masterName);
        List<String> slaveNames = new ArrayList<>();

        for (int i=0;i<slaveList.size();i++){
            String slaveName = RubberDbConstant.createSlaveDbName(masterName,i);
            shardingDbMap.put(slaveName,slaveList.get(i));
            slaveNames.add(slaveName);
        }
        masterSlaveRuleConfiguration.setSlaveDataSourceNames(slaveNames);
        masterSlaveRuleConfiguration.setLoadBalanceAlgorithm(new RandomMasterSlaveLoadBalanceAlgorithm());

        return masterSlaveRuleConfiguration;
    }



    /**
     * 创建dataSource对象
     */
    protected DruidDataSource createDruidDataSource(DbConnectProperties connect, DbConfigProperties config){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(connect.getUrl());
        datasource.setUsername(connect.getUsername());
        datasource.setPassword(connect.getPassword());
        datasource.setDriverClassName(connect.getDriverClassName());

        datasource.setInitialSize(config.getInitialSize());
        datasource.setMinIdle(config.getMinIdle());
        datasource.setMaxActive(config.getMaxActive());
        datasource.setMaxWait(config.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(config.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(config.getValidationQuery());
        datasource.setTestWhileIdle(config.getTestWhileIdle());
        datasource.setTestOnBorrow(config.getTestOnBorrow());
        datasource.setTestOnReturn(config.getTestOnReturn());
        datasource.setPoolPreparedStatements(config.getPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(config.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(config.getFilters());
        } catch (SQLException e) {
        }
        datasource.setConnectionProperties(config.getConnectionProperties());
        return  datasource;
    }


    /**
     * 创建从节点的配置信息
     */
    protected Map<String,List<DruidDataSource>> doCreateSlaveDateSource(String masterName,List<DbConnectProperties> slaveConnects,DbConfigProperties dbConfigProperties){
        if (CollUtil.isEmpty(slaveConnects)){
            return null;
        }
        Map<String,List<DruidDataSource>> slaves = new HashMap<>(2);
        List<DruidDataSource> slaveDataSourceArray = new ArrayList<>();
        for (DbConnectProperties slave:slaveConnects){
            slaveDataSourceArray.add(createDruidDataSource(slave,dbConfigProperties));
        }
        slaves.put(masterName,slaveDataSourceArray);
        return slaves;
    }


}
