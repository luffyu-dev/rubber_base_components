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


    public ZipkinRubberConfigLocator(NacosConfigManager nacosConfigManager, RubberProxyConfigProperties rubberProxyConfigProperties) {
        super(nacosConfigManager,rubberProxyConfigProperties);
    }

    @Override
    public Set<String> doCreateDataIds(Environment environment,String[] activeProfiles) {
        Set<String> redisDataId = new HashSet<>();
        if(getRubberProxyConfigProperties().isOpenZipkin()){
            for (String activeProfile:activeProfiles){
                redisDataId.add(activeProfile+"-rubber-config-zipkin.yml");
            }
        }
        //dev-rubber-config-zipkin.yml
        return redisDataId;
    }

    @Override
    public String configLocatorName() {
        return "rubberConfigZipkin";
    }


}
