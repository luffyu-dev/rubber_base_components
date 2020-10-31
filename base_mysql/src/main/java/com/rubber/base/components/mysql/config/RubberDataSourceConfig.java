package com.rubber.base.components.mysql.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.rubber.base.components.mysql.RubberDataSourceFactory;
import com.rubber.base.components.mysql.bean.DBShardingType;
import com.rubber.base.components.mysql.bean.RubberDataSourceBean;
import com.rubber.base.components.mysql.bean.RubberDataSourceProperties;
import com.rubber.base.components.mysql.sharding.MyDBShardingAlgorithm;
import com.rubber.base.components.mysql.sharding.MyTableShardingAlgorithm;
import io.shardingsphere.api.algorithm.masterslave.RandomMasterSlaveLoadBalanceAlgorithm;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
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


    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() throws SQLException {
        return handlerSharding();
    }


    public DataSource handlerSharding() throws SQLException {
        if (MapUtil.isEmpty(dataSource)){
            return null;
        }

        List<RubberDataSourceProperties> userDBDataSource = dataSource.get("userDB");
        if (CollUtil.isEmpty(userDBDataSource)){
            return null;
        }

        //定义一个分表规则
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        Map<String, DataSource> shardingDbMap = new HashMap<>(32);

        //db的配置
        for (RubberDataSourceProperties rubberDataSourceConfig:userDBDataSource){
            RubberDataSourceBean rubberDataSourceBean= RubberDataSourceFactory.creat(rubberDataSourceConfig);
            shardingDbMap.putAll(rubberDataSourceBean.getShardingDbMap());
            //如果是主从的化
            if (DBShardingType.MASTER_SLAVE.equals(rubberDataSourceBean.getShardingType())){
                MasterSlaveRuleConfiguration masterSlaveRuleConfiguration = new MasterSlaveRuleConfiguration();
                masterSlaveRuleConfiguration.setName(rubberDataSourceBean.getMasterName());
                masterSlaveRuleConfiguration.setMasterDataSourceName(rubberDataSourceBean.getMasterName());
                masterSlaveRuleConfiguration.setSlaveDataSourceNames(rubberDataSourceBean.getSlaveNames());
                masterSlaveRuleConfiguration.setLoadBalanceAlgorithm(new RandomMasterSlaveLoadBalanceAlgorithm());
                shardingRuleConfiguration.getMasterSlaveRuleConfigs().add(masterSlaveRuleConfiguration);
            }
        }

        //设置table的配置值
        TableRuleConfiguration tableConfiguration = new TableRuleConfiguration();
        tableConfiguration.setLogicTable("user_info");
        tableConfiguration.setActualDataNodes("test_00_db.user_info_0${0..2}");
        //定义数据库的分表规则
        StandardShardingStrategyConfiguration dbStrategyConfiguration = new StandardShardingStrategyConfiguration("uid",new MyDBShardingAlgorithm());
        tableConfiguration.setDatabaseShardingStrategyConfig(dbStrategyConfiguration);

        //定义table的分表规则
        StandardShardingStrategyConfiguration tableStrategyConfiguration = new StandardShardingStrategyConfiguration("uid",new MyTableShardingAlgorithm());
        tableConfiguration.setTableShardingStrategyConfig(tableStrategyConfiguration);


//        //设置table的配置值
//        TableRuleConfiguration tableConfiguration2 = new TableRuleConfiguration();
//        tableConfiguration2.setLogicTable("user_info");
//        tableConfiguration2.setActualDataNodes("test_00_db_slave_1.user_info_0${0..2}");
//        //定义数据库的分表规则
//        tableConfiguration2.setDatabaseShardingStrategyConfig(dbStrategyConfiguration);
//        //定义table的分表规则
//        tableConfiguration2.setTableShardingStrategyConfig(tableStrategyConfiguration);
//
//
//        //table的配置
//        shardingRuleConfiguration.getTableRuleConfigs().add(tableConfiguration);
//        shardingRuleConfiguration.getTableRuleConfigs().add(tableConfiguration2);


        return ShardingDataSourceFactory.createDataSource(shardingDbMap,shardingRuleConfiguration,new ConcurrentHashMap<>(),new Properties());

    }

}
