package com.rubber.base.components.mysql.plugins.sharding.config;

import com.rubber.base.components.mysql.plugins.sharding.strategy.RubberShardingStrategy;
import lombok.Getter;

/**
 * @author luffyu
 * Created on 2022/10/15
 */
@Getter
public class TableShardingConfig {

    /**
     * 逻辑库
     */
    private String logicDb;

    /**
     * 逻辑表
     */
    private String logicTable;

    /**
     * 真实的DB
     */
    private String realDb;

    /**
     * 真实的table
     */
    private String realTable;

    /**
     * 分库分表的策略
     */
    private RubberShardingStrategy strategy;


    private RubberShardingConfig rubberShardingConfig;


    public TableShardingConfig(String logicTable, String realDb, String realTable, RubberShardingStrategy strategy) {
        this.logicTable = logicTable;
        this.realDb = realDb;
        this.realTable = realTable;
        this.strategy = strategy;
    }

    public TableShardingConfig(String logicTable, RubberShardingConfig rubberShardingConfig) {
        this.logicTable = logicTable;
        this.rubberShardingConfig = rubberShardingConfig;
    }

    public TableShardingConfig logicTable(String logicTable){
        this.logicTable = logicTable;
        return this;
    }

    public TableShardingConfig logicDb(String logicDb){
        this.logicDb = logicDb;
        return this;
    }

    public TableShardingConfig realDb(String realDb){
        this.realDb = realDb;
        return this;
    }

    public TableShardingConfig realTable(String realTable){
        this.realTable = realTable;
        return this;
    }


    public TableShardingConfig strategy(RubberShardingStrategy strategy){
        this.strategy = strategy;
        return this;
    }


    public RubberShardingConfig end(){
        return this.rubberShardingConfig;
    }
}
