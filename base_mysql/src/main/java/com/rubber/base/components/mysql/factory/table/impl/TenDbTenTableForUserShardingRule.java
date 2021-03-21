package com.rubber.base.components.mysql.factory.table.impl;

import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.exception.TableRuleDbNotFoundException;
import com.rubber.base.components.mysql.factory.table.RubberTableRuleFactory;
import com.rubber.base.components.mysql.factory.table.sharding.TenDbTenTableForUserDbSharding;
import com.rubber.base.components.mysql.factory.table.sharding.TenDbTenTableForUserTableSharding;
import com.rubber.base.components.mysql.properties.TableConfigProperties;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class TenDbTenTableForUserShardingRule implements RubberTableRuleFactory{

    /**
     * 最大的index值
     */
    private final static int MAX_DB_INDEX = 10;

    /**
     * 通过工厂类创建一个 tableRuleConfig对象
     *
     * @param dbRule      当前db规则，包含已经创建好的数据库 和 配置规则
     * @param tableConfig 当前的表的配置规则
     * @return 返回一个sharding表的规则
     */
    @Override
    public TableRuleConfiguration createTableRule(RubberShardingRuleBean dbRule, TableConfigProperties tableConfig) {
        checkDbConfig(dbRule,tableConfig);

        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
        tableRuleConfiguration.setLogicTable(tableConfig.getLogicTableName());
        tableRuleConfiguration.setActualDataNodes(tableConfig.getDbName()+"_0${0..9}."+tableConfig.getLogicTableName()+"_0${0..9}");

        StandardShardingStrategyConfiguration tableStrategyConfiguration = new StandardShardingStrategyConfiguration("uid", new TenDbTenTableForUserTableSharding());
        tableRuleConfiguration.setTableShardingStrategyConfig(tableStrategyConfiguration);

        StandardShardingStrategyConfiguration dbStrategyConfiguration = new StandardShardingStrategyConfiguration(tableConfig.getShardingColumn(), new TenDbTenTableForUserDbSharding());
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(dbStrategyConfiguration);

        //添加总的分表规则
        dbRule.getShardingRuleConfiguration().getTableRuleConfigs().add(tableRuleConfiguration);
        return tableRuleConfiguration;
    }

    /**
     * 验证是否所有的db都存在
     * @param dbRule
     * @param tableConfig
     */
    private void checkDbConfig(RubberShardingRuleBean dbRule, TableConfigProperties tableConfig){
        String dbName = tableConfig.getDbName();
        for (int i=0;i<MAX_DB_INDEX;i++){
            String dbNameIndex = dbName + "_0"+i;
            if (dbRule.getDataSourceMap().get(dbNameIndex) == null){
                throw new TableRuleDbNotFoundException(dbNameIndex + " is not find in dbRules");
            }
        }
    }


}
