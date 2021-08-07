package com.rubber.base.components.mysql.factory.table.singlerule;

import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.exception.TableRuleDbNotFoundException;
import com.rubber.base.components.mysql.factory.table.RubberTableRuleFactory;
import com.rubber.base.components.mysql.properties.TableConfigProperties;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.NoneShardingStrategyConfiguration;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class SingleDbSingleTableRule implements RubberTableRuleFactory {
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
        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
        tableRuleConfiguration.setLogicTable(tableConfig.getLogicTableName());
        tableRuleConfiguration.setActualDataNodes(dbName+"."+tableConfig.getLogicTableName());
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(new NoneShardingStrategyConfiguration());
        dbRule.getShardingRuleConfiguration().getTableRuleConfigs().add(tableRuleConfiguration);
        return tableRuleConfiguration;
    }
}
