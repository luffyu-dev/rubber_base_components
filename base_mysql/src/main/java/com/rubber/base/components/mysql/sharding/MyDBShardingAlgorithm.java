package com.rubber.base.components.mysql.sharding;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * DB的分发规则
 * @author luffyu
 * Created on 2019-01-24
 */
public class MyDBShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String value = preciseShardingValue.getValue();
        if(collection != null && collection.size() > 0){
            return collection.iterator().next();
        }
        throw new IllegalArgumentException();
    }
}
