package com.rubber.base.components.mysql.plugins.sharding.strategy;

import com.rubber.base.components.mysql.plugins.sharding.bean.ShardingLogicTable;

/**
 *
 * 分库分表策略方法
 *
 * @author luffyu
 * Created on 2022/10/15
 */
public class NothingRubberShardingStrategy implements RubberShardingStrategy{


    /**
     * 路由切换的sharding
     *
     * @param table
     */
    @Override
    public ShardingLogicTable sharding(ShardingLogicTable table) {
        return table;
    }

}
