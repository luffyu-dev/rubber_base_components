package com.rubber.base.components.mysql.plugins.sharding;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * @author luffyu
 * Created on 2022/10/15
 */
public class ShardingLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {


    /**
     * 路由key
     */
    private Object shardingValue;


    public Object getShardingValue() {
        return shardingValue;
    }

    public void setShardingValue(Object shardingValue) {
        this.shardingValue = shardingValue;
    }
}
