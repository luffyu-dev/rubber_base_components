package com.rubber.base.components.mysql.bean;

import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import lombok.Data;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/12/27
 */
@Data
public class RubberShardingRuleBean {


    private Map<String, DataSource> dataSourceMap;


    private ShardingRuleConfiguration shardingRuleConfiguration;


    public RubberShardingRuleBean(Map<String, DataSource> dataSourceMap, ShardingRuleConfiguration shardingRuleConfiguration) {
        this.dataSourceMap = dataSourceMap;
        this.shardingRuleConfiguration = shardingRuleConfiguration;
    }
}
