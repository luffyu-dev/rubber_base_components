package com.rubber.base.components.mysql;

import com.rubber.base.components.mysql.bean.TableShardingType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/10/31
 */
public class ShardingConstantTools {

    /**
     * 默认的数据库配置值
     */
    private static String DEFAULT_DB_NAME ;


    /**
     * 表的类型对应关系
     */
    private static Map<String, TableShardingType> SHARDING_TABLE_TYPE = new HashMap<>();







    public static String getDefaultDbName() {
        return DEFAULT_DB_NAME;
    }

    public static void setDefaultDbName(String defaultDbName) {
        DEFAULT_DB_NAME = defaultDbName;
    }

    public static Map<String, TableShardingType> getShardingTableType() {
        return SHARDING_TABLE_TYPE;
    }

    public static void setShardingTableType(Map<String, TableShardingType> shardingTableType) {
        SHARDING_TABLE_TYPE = shardingTableType;
    }

    /**
     * 设置表的对应关系
     * @param tableName 当前表名称
     * @param shardingType 当前table的配置信息
     */
    public static void addShardingTableType(String tableName, TableShardingType shardingType){
        SHARDING_TABLE_TYPE.putIfAbsent(tableName,shardingType);
    }



}
