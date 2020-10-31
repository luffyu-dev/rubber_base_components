package com.rubber.base.components.mysql.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.rubber.base.components.mysql.Factory.RubberDataSourceFactory;
import com.rubber.base.components.mysql.Factory.RubberTableConfigFactory;
import com.rubber.base.components.mysql.bean.DBShardingType;
import com.rubber.base.components.mysql.bean.RubberDataSourceBean;
import com.rubber.base.components.mysql.bean.RubberDataSourceProperties;
import io.shardingsphere.api.algorithm.masterslave.RandomMasterSlaveLoadBalanceAlgorithm;
import io.shardingsphere.api.config.rule.MasterSlaveRuleConfiguration;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
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

            List<TableRuleConfiguration> tableConfig = RubberTableConfigFactory.createTableConfig(rubberDataSourceConfig);
            if (CollUtil.isNotEmpty(tableConfig)){
                shardingRuleConfiguration.getTableRuleConfigs().addAll(tableConfig);
            }

            //初始化table信息
        }
        return ShardingDataSourceFactory.createDataSource(shardingDbMap,shardingRuleConfiguration,new ConcurrentHashMap<>(),new Properties());

    }







}
