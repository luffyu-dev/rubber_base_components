package com.rubber.base.components.mysql.factory.table;

import cn.hutool.core.collection.CollUtil;
import com.rubber.base.components.mysql.bean.RubberShardingRuleBean;
import com.rubber.base.components.mysql.bean.RuleType;
import com.rubber.base.components.mysql.factory.table.impl.SingleDbSingleTableRule;
import com.rubber.base.components.mysql.factory.table.impl.SingleDbTenTableForHashRule;
import com.rubber.base.components.mysql.factory.table.impl.TenDbTenTableForUserShardingRule;
import com.rubber.base.components.mysql.factory.table.impl.YearDbDayTableRule;
import com.rubber.base.components.mysql.properties.TableConfigProperties;

import java.util.List;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class RubberTableRuleBuilder {

    private List<TableConfigProperties> tableConfigProperties;

    public RubberTableRuleBuilder(List<TableConfigProperties> tableConfigProperties) {
        this.tableConfigProperties = tableConfigProperties;
    }



    public void createTableRule(RubberShardingRuleBean dbRule){
        if (CollUtil.isNotEmpty(tableConfigProperties)){
            tableConfigProperties.forEach(i->{
                RubberTableRuleFactory factory = createByType(i.getRuleType());
                factory.createTableRule(dbRule,i);
            });
        }
    }

    private RubberTableRuleFactory createByType(RuleType ruleType){
        switch (ruleType){
            case SINGLE_DB_SINGLE_TABLE:
                return new SingleDbSingleTableRule();
            case SINGLE_DB_TEN_TABLE_HASH:
                return new SingleDbTenTableForHashRule();
            case TEN_DB_TEN_TABLE_USER:
                return new TenDbTenTableForUserShardingRule();
            case YEAR_DB_DAY_TABLE:
                return new YearDbDayTableRule();
            default:
        }
        return new SingleDbSingleTableRule();
    }
}
