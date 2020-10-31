package com.rubber.base.components.mysql.bean;

/**
 * @author luffyu
 * Created on 2020/10/31
 */
public enum TableShardingType {

    /**
     * 默认
     * 如果没有配置，则部分表
     * 否则按照传入的key值进行 分表
     */
    BY_USER,

    /**
     * 按照天分表
     * 例如 20201031
     */
    BY_DAY,

    /**
     * 按照月分表
     * 例如 202010
     */
    BY_MONTH,

    /**
     * 按照年分表
     * 例如 2020
     */
    BY_YEAR
}
