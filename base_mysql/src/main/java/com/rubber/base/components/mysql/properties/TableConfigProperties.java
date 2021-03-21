package com.rubber.base.components.mysql.properties;

import com.rubber.base.components.mysql.bean.RuleType;
import lombok.Data;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
@Data
public class TableConfigProperties {

    /**
     * 逻辑库表名称
     */
    private String logicTableName;

    /**
     * 实际表名称
     */
    private String actualTableName;

    /**
     * 分库分表的值
     */
    private String shardingColumn;

    /**
     * 所属db的名称
     */
    private String dbName;


    /**
     * 分表的规则类型
     */
    private RuleType ruleType;

    /**
     * 分表的最大值
     * 在ruleType = SINGLE_DB_TEN_TABLE_HASH的时候有效
     */
    private int tableNum;


}
