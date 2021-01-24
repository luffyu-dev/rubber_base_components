package com.rubber.base.components.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * 相关配置的集合名称
 *
 * @author luffyu
 * Created on 2020/12/13
 */
@Data
@Configuration
@ConfigurationProperties("rubber.proxy.config")
public class RubberProxyConfigProperties {

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
