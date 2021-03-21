package com.rubber.base.components.mysql.bean;

/**
 * 分表的规则类型
 * @author luffyu
 * Created on 2021/3/21
 */
public enum RuleType {

    /**
     * 单库单表
     */
    SINGLE_DB_SINGLE_TABLE,

    /**
     * 单库十表
     */
    SINGLE_DB_TEN_TABLE_HASH,


    /**
     * 基于uid的
     * 十库十表
     */
    TEN_DB_TEN_TABLE_USER,

    /**
     * 年库 天表
     */
    YEAR_DB_DAY_TABLE
}
