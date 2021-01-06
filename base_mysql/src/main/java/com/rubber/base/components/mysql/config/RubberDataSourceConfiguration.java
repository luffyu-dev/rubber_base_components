package com.rubber.base.components.mysql.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.exception.NotFoundDataSourceException;
import com.rubber.base.components.mysql.factory.RubberDataSourceFactory;
import com.rubber.base.components.mysql.factory.RubberDruidDataSourceFactoryBuilder;
import com.rubber.base.components.mysql.properties.RubberDbProperties;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
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
@ConfigurationProperties(prefix = "rubber.proxy")
public class RubberDataSourceConfiguration {

    private Map<String,RubberDbProperties> db;

    private String dbSetName ;


    @Primary
    @Bean(name = "dataSource")
    public DataSource dataSource() throws SQLException {
        if (MapUtil.isEmpty(db) || StrUtil.isEmpty(dbSetName)){
            throw  new NotFoundDataSourceException();
        }

        RubberDbProperties rubberDbProperties = db.get(dbSetName);
        RubberDataSourceFactory builder = RubberDruidDataSourceFactoryBuilder.builder(rubberDbProperties);
        RubberShardingRuleBean dbRule = builder.createDbRule(rubberDbProperties);
        return ShardingDataSourceFactory.createDataSource(dbRule.getDataSourceMap(),dbRule.getShardingRuleConfiguration(),new ConcurrentHashMap<>(16),new Properties());

    }




}
