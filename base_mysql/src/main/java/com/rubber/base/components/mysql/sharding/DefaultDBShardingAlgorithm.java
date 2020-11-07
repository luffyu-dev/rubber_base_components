package com.rubber.base.components.mysql.sharding;

import io.shardingsphere.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.hint.HintShardingAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;

/**
 * DB的分发规则
 * @author luffyu
 * Created on 2019-01-24
 */
@Slf4j
public class DefaultDBShardingAlgorithm implements HintShardingAlgorithm {

    private String defaultDbName;


    public DefaultDBShardingAlgorithm(String defaultDbName) {
        this.defaultDbName = defaultDbName;
    }

    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        System.out.println("走的默认db");
        return defaultDbName;
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, ShardingValue shardingValue) {
        System.out.println("走的默认db");
        return Arrays.asList(defaultDbName);
    }
}
