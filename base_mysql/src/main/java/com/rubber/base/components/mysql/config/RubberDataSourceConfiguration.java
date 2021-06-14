package com.rubber.base.components.mysql.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.exception.NotFoundDataSourceException;
import com.rubber.base.components.mysql.factory.db.RubberDataSourceFactory;
import com.rubber.base.components.mysql.factory.db.RubberDruidDataSourceFactoryBuilder;
import com.rubber.base.components.mysql.factory.table.RubberTableRuleBuilder;
import com.rubber.base.components.mysql.properties.DbConfigProperties;
import com.rubber.base.components.mysql.properties.RubberDbProperties;
import com.rubber.base.components.mysql.properties.TableConfigProperties;
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
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luffyu
 * Created on 2020/12/27
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "rubber.proxy.config")
public class RubberDataSourceConfiguration {

    private Map<String,RubberDbProperties> dbInstances;

    @Value("${rubber.proxy.set.dbInstance}")
    private String setDbInstance ;

    private List<TableConfigProperties> tableConfig;


    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource(RubberTableRuleBuilder ruleBuilder) throws SQLException {
        if (MapUtil.isEmpty(dbInstances) || StrUtil.isEmpty(setDbInstance)){
            throw  new NotFoundDataSourceException();
        }
        RubberDbProperties rubberDbProperties = getDbPropertyByName(setDbInstance);
        RubberDataSourceFactory builder = RubberDruidDataSourceFactoryBuilder.builder(rubberDbProperties);
        RubberShardingRuleBean dbRule = builder.createDbRule(rubberDbProperties);
        //创建表的规则
        ruleBuilder.createTableRule(dbRule);
        return ShardingDataSourceFactory.createDataSource(dbRule.getDataSourceMap(),dbRule.getShardingRuleConfiguration(),new ConcurrentHashMap<>(16),new Properties());
    }

    /**
     * 通过配置的实例名称 获取实例的配置信息
     * @param name 实例名称
     * @return 返回配置信息
     */
    private RubberDbProperties getDbPropertyByName(String name){
        RubberDbProperties rubberDbProperties = dbInstances.get(name);
        if (rubberDbProperties.getConfig() == null){
            rubberDbProperties.setConfig(new DbConfigProperties());
        }
        return rubberDbProperties;
    }


    @Bean
    public RubberTableRuleBuilder tableRuleBuilder(){
        return new RubberTableRuleBuilder(tableConfig);
    }



}
