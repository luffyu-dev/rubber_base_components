package com.rubber.base.components.mysql.factory.table;

import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.properties.TableConfigProperties;
import io.shardingsphere.api.config.rule.TableRuleConfiguration;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public interface RubberTableRuleFactory {

    /**
     * 通过工厂类创建一个 tableRuleConfig对象
     * @param dbRule 当前db规则，包含已经创建好的数据库 和 配置规则
     * @param tableConfig 当前的表的配置规则
     * @return 返回一个sharding表的规则
     */
    TableRuleConfiguration createTableRule(RubberShardingRuleBean dbRule, TableConfigProperties tableConfig);
}
