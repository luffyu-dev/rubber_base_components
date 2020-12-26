package com.rubber.base.components.mysql.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.base.components.mysql.bean.DBClusterType;
import com.rubber.base.components.mysql.bean.RubberDataSourceBean;
import com.rubber.base.components.mysql.exception.NotFoundDataSourceException;
import com.rubber.base.components.mysql.exception.NotFoundDefaultDataSourceException;
import com.rubber.base.components.mysql.factory.RubberDataSourceFactory;
import com.rubber.base.components.mysql.factory.RubberTableConfigFactory;
import com.rubber.base.components.mysql.sharding.DefaultDBShardingAlgorithm;
import com.rubber.base.components.mysql.sharding.MyDBShardingAlgorithm;
import io.shardingsphere.api.algorithm.masterslave.RandomMasterSlaveLoadBalanceAlgorithm;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.HintShardingStrategyConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luffyu
 * Created on 2020/10/29
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "rubber.sharding")
public class RubberDataSourceConfig {


    private Map<String, List<RubberDataSourceProperties>> dataSource;


    @Value("rubber.sharding.dbSetNames")
    private String dbSetNames ;


    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() throws SQLException {
        return handlerSharding();
    }


    public DataSource handlerSharding() throws SQLException {
        //获取数据源配置
        List<RubberDataSourceProperties> userDBDataSource = getDbSourceProperties();
        //定义一个分表规则
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        Map<String, DataSource> shardingDbMap = new HashMap<>(32);

        //db的配置
        for (RubberDataSourceProperties rubberDataSourceConfig:userDBDataSource){
            RubberDataSourceBean rubberDataSourceBean= RubberDataSourceFactory.creat(rubberDataSourceConfig);
            shardingDbMap.putAll(rubberDataSourceBean.getShardingDbMap());
            //如果是主从的化
            if (DBClusterType.MASTER_SLAVE.equals(rubberDataSourceBean.getShardingType())){
                MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = new MasterSlaveRuleConfiguration();
                masterSlaveRuleConfiguration.setName(rubberDataSourceBean.getMasterName());
                masterSlaveRuleConfiguration.setMasterDataSourceName(rubberDataSourceBean.getMasterName());
                masterSlaveRuleConfiguration.setSlaveDataSourceNames(rubberDataSourceBean.getSlaveNames());
                masterSlaveRuleConfiguration.setLoadBalanceAlgorithm(new RandomMasterSlaveLoadBalanceAlgorithm());
                shardingRuleConfiguration.getMasterSlaveRuleConfigs().add(masterSlaveRuleConfiguration);
            }

            //默认分片有问题
            // TODO: 2020/11/7 默认分片有点问题
            if (StrUtil.isEmpty(shardingRuleConfiguration.getDefaultDataSourceName())){
                if (StrUtil.isEmpty(rubberDataSourceBean.getDefaultName())){
                    throw new NotFoundDefaultDataSourceException();
                }
                shardingRuleConfiguration.setDefaultDataSourceName(rubberDataSourceBean.getDefaultName());
                shardingRuleConfiguration.setDefaultDatabaseShardingStrategyConfig(
                        new HintShardingStrategyConfiguration(new DefaultDBShardingAlgorithm(rubberDataSourceBean.getDefaultName()))
                );

            }
            List<TableRuleConfiguration> tableConfig = RubberTableConfigFactory.createTableConfig(rubberDataSourceConfig);
            if (CollUtil.isNotEmpty(tableConfig)){
                shardingRuleConfiguration.getTableRuleConfigs().addAll(tableConfig);
            }

            //初始化table信息
        }
        return ShardingDataSourceFactory.createDataSource(shardingDbMap,shardingRuleConfiguration,new ConcurrentHashMap<>(),new Properties());

    }



    private StandardShardingStrategyConfiguration setDefaultShardingRuleConfig(String defaultDbName,Map<String,DataSource> stringDataSourceMap){
        return new StandardShardingStrategyConfiguration("default",new MyDBShardingAlgorithm());
    }





    /**
     * 获取当前的dataSource信息
     * @return 返回配置的数据源
     */
    private List<RubberDataSourceProperties> getDbSourceProperties(){
        if (MapUtil.isEmpty(dataSource)){
            throw new NotFoundDataSourceException();
        }
        if (StrUtil.isEmpty(this.dbSetNames)){
            throw new NotFoundDataSourceException(" not config dbSetNames");
        }

        List<RubberDataSourceProperties> list = new ArrayList<>();
        String[] dbSetNameArray = dbSetNames.split(",");
        for(String dbName:dbSetNameArray){
            List<RubberDataSourceProperties> dbDataSource = dataSource.get(dbName);
            if (CollUtil.isEmpty(dbDataSource)){
                throw new NotFoundDataSourceException(" not config dbName connection for " + dbName);
            }
            list.addAll(dbDataSource);
        }
        return list;
    }



}
