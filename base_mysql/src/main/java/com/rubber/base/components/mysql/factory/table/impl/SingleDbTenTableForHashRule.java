package com.rubber.base.components.mysql.factory.table.impl;

import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.constant.RubberDbConstant;
import com.rubber.base.components.mysql.exception.TableRuleDbNotFoundException;
import com.rubber.base.components.mysql.factory.table.RubberTableRuleFactory;
import com.rubber.base.components.mysql.properties.TableConfigProperties;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;

import java.util.Collection;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class SingleDbTenTableForHashRule implements RubberTableRuleFactory, PreciseShardingAlgorithm<String> {

    /**
     * 通过工厂类创建一个 tableRuleConfig对象
     *
     * @param dbRule      当前db规则，包含已经创建好的数据库 和 配置规则
     * @param tableConfig 当前的表的配置规则
     * @return 返回一个sharding表的规则
     */
    @Override
    public TableRuleConfiguration createTableRule(RubberShardingRuleBean dbRule, TableConfigProperties tableConfig) {
        String dbName = tableConfig.getDbName();
        if (dbRule.getDataSourceMap().get(dbName) == null){
            throw new TableRuleDbNotFoundException(dbName + " is not find in dbRules");
        }
        int tableIndex = tableConfig.getTableNum();
        if (tableIndex <= 0 || tableIndex >= 10){
            tableIndex = 10;
        }

        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
        tableRuleConfiguration.setLogicTable(tableConfig.getLogicTableName());
        tableRuleConfiguration.setActualDataNodes(dbName+"."+tableConfig.getLogicTableName()+"_0${0.."+(tableIndex-1)+"}");

        StandardShardingStrategyConfiguration tableStrategyConfiguration = new StandardShardingStrategyConfiguration(tableConfig.getShardingColumn(), this);
        tableRuleConfiguration.setTableShardingStrategyConfig(tableStrategyConfiguration);

        //添加总的分表规则
        dbRule.getShardingRuleConfiguration().getTableRuleConfigs().add(tableRuleConfiguration);

        return tableRuleConfiguration;
    }

    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding result for data source or table's name
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        String value = String.valueOf(shardingValue.getValue());
        Integer index = RubberDbConstant.modHash(value, availableTargetNames.size());
        for (String each : availableTargetNames) {
            if (each.endsWith(String.valueOf(index))) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

}
