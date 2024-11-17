package com.rubber.base.components.mysql.plugins.sharding.strategy;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.rubber.base.components.mysql.plugins.sharding.bean.ShardingLogicTable;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author luffyu
 * Created on 2024/11/17
 */
@Slf4j
public class RubberYearMonthShardingStrategy implements RubberShardingStrategy{

    final String shardingIndex = "shardingValue";

    /**
     * 路由切换的sharding
     *
     * @param table
     */
    @Override
    public ShardingLogicTable sharding(ShardingLogicTable table) {
        Map<String, Object> params = table.getParams();
        if (params != null && params.containsKey(shardingIndex)){
            try {
                DateTime dateTime = DateUtil.parse(String.valueOf(params.get(shardingIndex)), "yyyyMMdd");
                String year =  DateUtil.format(dateTime,"yyyy");
                String month = DateUtil.format(dateTime,"MM");

                String realDb = table.getLogicDb() + "_" + year;
                String realTable = table.getLogicTable() + "_" + month;

                log.debug("RubberYearMonthShardingStrategy-db-{}-{}", table.getLogicDb(), realDb);
                log.debug("RubberYearMonthShardingStrategy-table-{}-{}", table.getLogicTable(), realTable);

                table.setRealDb(realDb);
                table.setRealTable(realTable);
            }catch (Exception e){
                log.error("RubberYearMonthShardingStrategy ",e);
            }
        }
        return table;
    }
}
