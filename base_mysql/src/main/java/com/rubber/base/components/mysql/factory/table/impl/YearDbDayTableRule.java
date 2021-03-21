package com.rubber.base.components.mysql.factory.table.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.exception.TableRuleDbNotFoundException;
import com.rubber.base.components.mysql.factory.table.RubberTableRuleFactory;
import com.rubber.base.components.mysql.factory.table.sharding.DayTableSharding;
import com.rubber.base.components.mysql.factory.table.sharding.YearDbSharding;
import com.rubber.base.components.mysql.properties.TableConfigProperties;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;
import io.shardingsphere.api.config.strategy.StandardShardingStrategyConfiguration;

/**
 * @author luffyu
 * Created on 2021/3/21
 * 年库天表的的配置信息
 */
public class YearDbDayTableRule implements RubberTableRuleFactory {
    /**
     * 通过工厂类创建一个 tableRuleConfig对象
     *
     * @param dbRule      当前db规则，包含已经创建好的数据库 和 配置规则
     * @param tableConfig 当前的表的配置规则
     * @return 返回一个sharding表的规则
     */
    @Override
    public TableRuleConfiguration createTableRule(RubberShardingRuleBean dbRule, TableConfigProperties tableConfig) {
        int year = DateUtil.year(new DateTime());
        String yearIndex= tableConfig.getDbName() + "_" + year;
        if (dbRule.getDataSourceMap().get(yearIndex) == null){
            throw new TableRuleDbNotFoundException(yearIndex + " is not find in dbRules");
        }

        TableRuleConfiguration tableRuleConfiguration = new TableRuleConfiguration();
        tableRuleConfiguration.setLogicTable(tableConfig.getLogicTableName());
        tableRuleConfiguration.setActualDataNodes(yearIndex+"."+tableConfig.getLogicTableName());

        StandardShardingStrategyConfiguration tableStrategyConfiguration = new StandardShardingStrategyConfiguration("creatTime", new DayTableSharding());
        tableRuleConfiguration.setTableShardingStrategyConfig(tableStrategyConfiguration);

        StandardShardingStrategyConfiguration dbStrategyConfiguration = new StandardShardingStrategyConfiguration("creatTime", new YearDbSharding());
        tableRuleConfiguration.setDatabaseShardingStrategyConfig(dbStrategyConfiguration);

        //添加总的分表规则
        dbRule.getShardingRuleConfiguration().getTableRuleConfigs().add(tableRuleConfiguration);
        return tableRuleConfiguration;
    }
}
