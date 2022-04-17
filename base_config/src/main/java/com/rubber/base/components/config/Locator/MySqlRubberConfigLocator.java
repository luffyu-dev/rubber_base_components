package com.rubber.base.components.config.Locator;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import org.springframework.core.annotation.Order;

/**
 * @author luffyu
 * Created on 2021/1/24
 */
@Order(1)
public class MySqlRubberConfigLocator extends BaseRubberConfigLocator {

    /**
     * mysql的配置前缀
     */
    private static final String MYSQL_CONFIG_PREFIX = "mysql";


    public MySqlRubberConfigLocator(NacosConfigManager nacosConfigManager,RubberProxyConfigProperties rubberProxyConfigProperties) {
       super(nacosConfigManager,rubberProxyConfigProperties);
    }

    /**
     * 获取集群的key指
     *
     * @return 返回一个逗号隔开的集群
     */
    @Override
    public String getInstanceKey() {
        return getRubberProxyConfigProperties().getDbInstance();
    }

    @Override
    public String configLocatorName() {
        return MYSQL_CONFIG_PREFIX;
    }
}
