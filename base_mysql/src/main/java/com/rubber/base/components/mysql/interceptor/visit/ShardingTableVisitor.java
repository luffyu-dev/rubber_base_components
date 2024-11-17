package com.rubber.base.components.mysql.interceptor.visit;

import com.alibaba.druid.sql.ast.statement.SQLExportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.rubber.base.components.mysql.plugins.sharding.config.TableShardingConfig;
import com.rubber.base.components.mysql.plugins.sharding.strategy.RubberShardingStrategy;
import com.rubber.base.components.mysql.plugins.sharding.bean.ShardingLogicTable;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2022/10/14
 */
public class ShardingTableVisitor extends MySqlASTVisitorAdapter {


    /**
     * 基于表的分库分表配置
     */
    private Map<String, TableShardingConfig> tableShardingConfig;

    /**
     * 当前的请求入参
     */
    private Map<String,Object> params;


    public ShardingTableVisitor(Map<String, TableShardingConfig> tableShardingConfig, Map<String,Object> params) {
        this.tableShardingConfig = tableShardingConfig;
        this.params = params;
    }



    @Override
    public boolean visit(SQLExprTableSource tableSource) {
        String tableName = tableSource.getTableName();
        // 设置逻辑表
        TableShardingConfig shardingConfig = tableShardingConfig.get(tableName);
        if(shardingConfig != null){
            ShardingLogicTable shardingLogicTable = new ShardingLogicTable();
            shardingLogicTable.setLogicDb(shardingConfig.getLogicDb());
            shardingLogicTable.setLogicTable(shardingConfig.getLogicTable());
            shardingLogicTable.setRealDb(shardingConfig.getRealDb());
            shardingLogicTable.setRealTable(shardingConfig.getRealTable());
            shardingLogicTable.setParams(this.params);

            RubberShardingStrategy shardingStrategy = shardingConfig.getStrategy();
            if (shardingStrategy != null){
                shardingLogicTable = shardingStrategy.sharding(shardingLogicTable);
            }
            tableName = shardingLogicTable.tableName();
        }
        tableSource.setSimpleName(tableName);
        return true;
    }


    @Override
    public void endVisit(SQLExportTableStatement statement){

    }




}
