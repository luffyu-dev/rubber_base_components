package com.rubber.base.components.mysql.sharding;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.standard.PreciseShardingAlgorithm;

import java.util.Collection;

/**
 * 表的分发规则
 * @author luffyu
 * Created on 2019-01-24
 */
public class MyTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        String value = preciseShardingValue.getValue();
        for (String each : collection) {
            Integer integer = modHash(value, collection.size());
            if (each.endsWith(String.valueOf(integer))) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }


    private static Integer modHash(String str,int index){
        int hash = str.toLowerCase().hashCode();
        if(hash <= 0){
            hash = -hash;
        }
        return hash % index;
    }

}
