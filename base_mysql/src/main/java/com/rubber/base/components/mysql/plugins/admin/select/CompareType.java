package com.rubber.base.components.mysql.plugins.admin.select;

/**
 * @author luffyu
 * Created on 2019-05-27
 */
public enum CompareType {
    /**
     * 比较的关键字
     */

    like("LIKE"),
    /**
     * 等于
     */
    eq("="),
    /**
     * 不等于
     */
    ne("<>"),
    /**
     * 大于
     */
    gt(">"),
    /**
     * 大于等于
     */
    ge(">="),
    /**
     * 小于
     */
    lt("<"),
    /**
     * 小于等于
     */
    le("<="),
    /**
     * 在什么之间
     */
    between("BETWEEN"),

    ;

    public String key;

    CompareType(final String key) {
        this.key = key;
    }
}
