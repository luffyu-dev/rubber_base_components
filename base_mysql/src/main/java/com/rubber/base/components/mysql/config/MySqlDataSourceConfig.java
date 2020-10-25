package com.rubber.base.components.mysql.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.rubber.base.components.mysql.bean.RubberDruidDataSource;
import com.rubber.base.components.tools.SpringTools;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/10/25
 */
@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "rubber")
public class MySqlDataSourceConfig {


    private Map<String,Map<String,RubberDruidDataSource>> sharding;


    @Bean(name = "dataSource")
    public DataSource dataSource() throws SQLException {
        return createDataSource();
    }


    /**
     * 创建数据库
     */
    private DruidDataSource createDataSource() {
        String envKey = SpringTools.getEnvKey();
        if (StrUtil.isEmpty(envKey) || MapUtil.isEmpty(sharding)){
            log.error("当前应用没有配置环境 或者 没有配置数据库分片信息");
            return null;
        }
        Map<String,RubberDruidDataSource> envDataSource = sharding.get(envKey);
        if (MapUtil.isEmpty(envDataSource)){
            log.error("当前环境{},没有配置数据库连接配置",envKey);
            return null;
        }

        //数据库名称规范：env_dbClusterName_dbName_00

        //数据库表名称，来看是否开启分片

        //主从怎么配置呢？

        Map<String,RubberDruidDataSource> shardingDbMap = new HashMap<>();
        for (Map.Entry<String,RubberDruidDataSource> map:envDataSource.entrySet()){
            String dbClusterName = map.getKey();
            RubberDruidDataSource sourceConfig = map.getValue();
            //master 和 slaves 可能为空
            List<RubberDruidDataSource> masterList = createDataSourceConfig(envKey,dbClusterName,sourceConfig,true);
            List<RubberDruidDataSource> slavesList = createDataSourceConfig(envKey,dbClusterName,sourceConfig,false);
            String shardingDbName = envKey + "_" + dbClusterName + "_" + sourceConfig.getDbName();
            shardingDbMap.put(shardingDbName,masterList.get(0));
        }

        //设置table的配置值
        TableRuleConfiguration tableConfiguration = new TableRuleConfiguration();

        //在进行数据分片？？

        //定义数据库的分表规则


        System.out.println(sharding);
        return null;
    }



    /**
     * 创建数据库
     */
    private List<RubberDruidDataSource> createDataSourceConfig(String envKey,String setDBName,RubberDruidDataSource sourceConfig, boolean isMaster) {
        List<RubberDruidDataSource> dataSources = new ArrayList<>();
        if (isMaster){
            RubberDruidDataSource masterDataSource = new RubberDruidDataSource();

            BeanUtils.copyProperties(sourceConfig,masterDataSource);
            RubberDruidDataSource masterConfig = sourceConfig.getMaster();


            masterDataSource.setDriver(masterConfig.getDriver());
            masterDataSource.setUrl(masterConfig.getUrl());
            masterDataSource.setUsername(masterConfig.getUsername());
            masterDataSource.setPassword(masterConfig.getPassword());
            masterDataSource.setDbClusterName(setDBName);
            masterDataSource.setMaster(true);
            masterDataSource.setEnv(envKey);
            return dataSources;
        }

        Map<String,RubberDruidDataSource> slaveDataSources = sourceConfig.getSlaves();
        if (CollUtil.isNotEmpty(slaveDataSources)){
            for (RubberDruidDataSource slaveSource:slaveDataSources.values()){
                RubberDruidDataSource slaveDataSource = new RubberDruidDataSource();

                BeanUtils.copyProperties(sourceConfig,slaveDataSource);

                slaveDataSource.setDriver(slaveSource.getDriver());
                slaveDataSource.setUrl(slaveSource.getUrl());
                slaveDataSource.setUsername(slaveSource.getUsername());
                slaveDataSource.setPassword(slaveSource.getPassword());
                slaveDataSource.setDbClusterName(setDBName);
                slaveDataSource.setMaster(false);
                slaveDataSource.setEnv(envKey);
                dataSources.add(slaveDataSource);
            }
        }
        return dataSources;

    }

    private  void  handlerDataSource(RubberDruidDataSource firstSource,RubberDruidDataSource secondSource){
        if (firstSource.getInitialSize() <=0 && secondSource.getInitialSize() > 0 ){
            firstSource.setInitialSize(secondSource.getInitialSize());
        }

        if (firstSource.getMinIdle() <=0 && secondSource.getMinIdle() > 0 ){
            firstSource.setMinIdle(secondSource.getMinIdle());
        }
        if (firstSource.getMaxActive() <=0 && secondSource.getMaxActive() > 0 ){
            firstSource.setMaxActive(secondSource.getMaxActive());
        }
        if (firstSource.getMaxWait() <=0 && secondSource.getMaxWait() > 0 ){
            firstSource.setMaxWait(secondSource.getMaxWait());
        }
        if (firstSource.getTimeBetweenEvictionRunsMillis() <=0 && secondSource.getTimeBetweenEvictionRunsMillis() > 0 ){
            firstSource.setTimeBetweenEvictionRunsMillis(secondSource.getTimeBetweenEvictionRunsMillis());
        }

        if (firstSource.getMinEvictableIdleTimeMillis() <=0 && secondSource.getMinEvictableIdleTimeMillis() > 0 ){
            firstSource.setMinEvictableIdleTimeMillis(secondSource.getMinEvictableIdleTimeMillis());
        }
        if (StrUtil.isEmpty(firstSource.getValidationQuery()) && StrUtil.isNotEmpty(secondSource.getValidationQuery())){
            firstSource.setValidationQuery(secondSource.getValidationQuery());
        }

    }


}
