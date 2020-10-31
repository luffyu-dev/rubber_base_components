package com.rubber.base.components.mysql.bean;

import lombok.Data;

/**
 * @author luffyu
 * Created on 2020/10/31
 */
@Data
public class RubberTableProperties {

    /**
     * 当前表所在库的节点信息
     * 例如 table_db_${0~9}
     */
    private String dbNodes;

    /**
     * 当前数据库的名称
     */
    private String tableName;

    /**
     * 当前shardingTable的名称
     * 例如 table_${0~9}
     */
    private String actualDataNodes;


    /**
     * 当前分表的类型
     * 例如 按照uid进行分表
     */
    private TableShardingType shardingType = TableShardingType.BY_USER;
}
