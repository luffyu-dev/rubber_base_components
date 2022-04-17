package com.rubber.base.components.config.Locator;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import org.springframework.core.annotation.Order;

/**
 * @author luffyu
 * Created on 2021/1/24
 */
@Order(2)
public class RedisRubberConfigLocator extends BaseRubberConfigLocator {

    /**
     * mysql的配置前缀
     */
    private static final String REDIS_CONFIG_PREFIX = "REDIS";


    public RedisRubberConfigLocator(NacosConfigManager nacosConfigManager, RubberProxyConfigProperties rubberProxyConfigProperties) {
        super(nacosConfigManager,rubberProxyConfigProperties);
    }

    /**
     * 获取集群的key指
     *
     * @return 返回一个逗号隔开的集群
     */
    @Override
    public String getInstanceKey() {
        return getRubberProxyConfigProperties().getRedisInstance();
    }

    @Override
    public String configLocatorName() {
        return REDIS_CONFIG_PREFIX;
    }


}
