package com.rubber.base.components.config.Locator;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.HashSet;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/1/24
 */
@Order(3)
public class ZipkinRubberConfigLocator extends BaseRubberConfigLocator {

    /**
     * mysql的配置前缀
     */
    private static final String ZIPKIN_CONFIG_PREFIX = "ZIPKIN";


    public ZipkinRubberConfigLocator(NacosConfigManager nacosConfigManager, RubberProxyConfigProperties rubberProxyConfigProperties) {
        super(nacosConfigManager,rubberProxyConfigProperties);
    }

    /**
     * 获取集群的key指
     *
     * @return 返回一个逗号隔开的集群
     */
    @Override
    public String getInstanceKey() {
        if(getRubberProxyConfigProperties().isOpenZipkin()){
            return "default";
        }
        return null;
    }

    @Override
    public String configLocatorName() {
        return ZIPKIN_CONFIG_PREFIX;
    }


}
