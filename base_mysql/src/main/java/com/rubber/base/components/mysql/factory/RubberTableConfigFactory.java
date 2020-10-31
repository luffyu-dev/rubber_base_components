package com.rubber.base.components.mysql.factory;

import cn.hutool.core.collection.CollUtil;
import com.rubber.base.components.mysql.bean.RubberDataSourceProperties;
import com.rubber.base.components.mysql.bean.RubberTableProperties;
import com.rubber.base.components.mysql.sharding.MyDBShardingAlgorithm;
import com.rubber.base.components.mysql.sharding.MyTableShardingAlgorithm;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luffyu
 * Created on 2020/10/31
 */
public class RubberTableConfigFactory {


    /**
     * 创建table的config信息
     * @param rubberDataSourceProperties 当前rubber的data配置信息
     * @return 返回rubberTable的config信息
     */
    public static List<TableRuleConfiguration> createTableConfig(RubberDataSourceProperties rubberDataSourceProperties){
        List<RubberTableProperties> tables = rubberDataSourceProperties.getTables();
        if (CollUtil.isEmpty(tables)){
            return null;
        }
        List<TableRuleConfiguration> tablesConfig = new ArrayList<>(tables.size());
        for (RubberTableProperties tableProperties:tables){
            //设置table的配置值
            TableRuleConfiguration tableConfiguration = new TableRuleConfiguration();
            tableConfiguration.setLogicTable(tableProperties.getTableName());
            tableConfiguration.setActualDataNodes(tableProperties.getActualDataNodes());
            //定义数据库的分表规则
            StandardShardingStrategyConfiguration dbStrategyConfiguration = new StandardShardingStrategyConfiguration("uid",new MyDBShardingAlgorithm());
            tableConfiguration.setDatabaseShardingStrategyConfig(dbStrategyConfiguration);
            //定义table的分表规则
            StandardShardingStrategyConfiguration tableStrategyConfiguration = new StandardShardingStrategyConfiguration("uid",new MyTableShardingAlgorithm());
            tableConfiguration.setTableShardingStrategyConfig(tableStrategyConfiguration);
            tablesConfig.add(tableConfiguration);
        }
        return tablesConfig;
    }

}
