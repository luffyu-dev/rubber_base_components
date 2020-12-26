package com.rubber.base.components.mysql.bean;

/**
 * @author luffyu
 * Created on 2020/10/28
 */
public enum DBClusterType {


    /**
     * 单库
     */
    SINGLE,

    /**
     * 多库
     */
    CLUSTER,

    /**
     * 主从库
     */
    MASTER_SLAVE

}
