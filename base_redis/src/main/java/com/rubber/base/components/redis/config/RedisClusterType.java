package com.rubber.base.components.redis.config;

/**
 * @author luffyu
 * Created on 2020/10/28
 */
public enum RedisClusterType {


    /**
     * 单例
     */
    SINGLE,

    /**
     * 主从
     */
    MASTER_SLAVE,

    /**
     * 集群
     */
    CLUSTER
}
