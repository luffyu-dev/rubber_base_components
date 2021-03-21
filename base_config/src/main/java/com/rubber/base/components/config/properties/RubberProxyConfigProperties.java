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
@ConfigurationProperties("rubber.proxy.set")
public class RubberProxyConfigProperties {

    /**
     * db配置的集合名称
     */
    private String dbInstance;

    /**
     * redis配置的集合名称
     */
    private String redisInstance;

    /**
     * mq配置的集合名称
     */
    private String rocketMqInstance;

    /**
     * 是否开启调用链路
     */
    private boolean openZipkin = true;

}
