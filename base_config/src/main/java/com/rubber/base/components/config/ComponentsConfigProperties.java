package com.rubber.base.components.config;

import lombok.Data;

/**
 *
 * 相关配置的集合名称
 *
 * @author luffyu
 * Created on 2020/12/13
 */
@Data
public class ComponentsConfigProperties {

    /**
     * db配置的集合名称
     */
    private String dbSet;

    /**
     * redis配置的集合名称
     */
    private String redisSet;

    /**
     * mq配置的集合名称
     */
    private String rocketMqSet;


}
