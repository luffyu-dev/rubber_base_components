package com.rubber.base.components.mysql.factory.impl;

import com.rubber.base.components.mysql.exception.NotSingleDataSourceException;
import com.rubber.base.components.mysql.factory.BaseRubberDateSourceFactory;
import com.rubber.base.components.mysql.properties.RubberDbProperties;
import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;

/**
 * @author luffyu
 * Created on 2020/12/26
 */
public class RubberSingleDataSourceFactory extends BaseRubberDateSourceFactory {



    /**
     * 检测配置
     */
    @Override
    protected void checkDbProperties(RubberDbProperties rubberDbProperties){
        super.checkDbProperties(rubberDbProperties);
        if (rubberDbProperties.getConnects().size() != 1){
            throw new NotSingleDataSourceException();
        }
    }


    @Override
    public void doCreateTableRule(ShardingRuleConfiguration shardingRuleConfiguration) {

    }
}
