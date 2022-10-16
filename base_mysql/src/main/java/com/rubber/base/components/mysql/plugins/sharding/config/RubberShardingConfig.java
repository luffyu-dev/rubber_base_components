package com.rubber.base.components.mysql.plugins.sharding.config;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2022/10/15
 */
@Getter
public class RubberShardingConfig {



    private Map<String, TableShardingConfig> rubberShardingConfig;


    public RubberShardingConfig() {
        this.rubberShardingConfig = new HashMap<>();
    }


    public TableShardingConfig table(String tableName){
        TableShardingConfig tableShardingConfig = new TableShardingConfig(tableName,this);
        rubberShardingConfig.put(tableName,tableShardingConfig);
        return tableShardingConfig;
    }


    public RubberShardingConfig add(TableShardingConfig config){
        rubberShardingConfig.put(config.getLogicTable(),config);
        return this;
    }


}
