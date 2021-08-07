package com.rubber.base.components.mysql.factory.table.hashRule.sharding;

import com.rubber.base.components.mysql.exception.ShardingUidIllegalException;
import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class TenDbTenTableForUserDbSharding implements PreciseShardingAlgorithm<Integer> {
    /**
     * Sharding.
     *
     * @param availableTargetNames available data sources or tables's names
     * @param shardingValue        sharding value
     * @return sharding result for data source or table's name
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Integer> shardingValue) {
        Integer uid = shardingValue.getValue();
        if (uid == null || uid < 10000){
            throw new ShardingUidIllegalException(uid+" is illegal");
        }
        //读取倒数第三位数字
        int index = uid / 100 % 10;
        String indexStr = "_0"+index;
        for (String each : availableTargetNames) {
            if (each.endsWith(indexStr)) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }
}
