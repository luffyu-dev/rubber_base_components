package com.rubber.base.components.mysql.factory.table.sharding;

import cn.hutool.core.date.DateUtil;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;
import java.util.Date;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class DayTableSharding implements PreciseShardingAlgorithm<Date> {

    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding result for data source or table's name
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        Date value = new Date();
        if (shardingValue.getValue() != null){
            value = new Date();
        }
        String dayIndex = DateUtil.format(value,"MMdd");
        String prefix = availableTargetNames.iterator().next();
        return prefix + "_" + dayIndex;
    }

}
