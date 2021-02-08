package com.rubber.base.components.config.Locator;

import cn.hutool.core.util.StrUtil;
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


    public ZipkinRubberConfigLocator(NacosConfigManager nacosConfigManager, RubberProxyConfigProperties rubberProxyConfigProperties) {
        super(nacosConfigManager,rubberProxyConfigProperties);
    }

    @Override
    public Set<String> createDataIds(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();

        Set<String> redisDataId = new HashSet<>();
        for (String activeProfile:activeProfiles){
            redisDataId.add(activeProfile+"-rubber-config-zipkin.yml");
        }
        //dev-rubber-config-zipkin.yml
        return redisDataId;
    }

    @Override
    public String configLocatorName() {
        return "rubberConfigZipkin";
    }


}
