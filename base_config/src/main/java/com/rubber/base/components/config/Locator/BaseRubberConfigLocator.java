package com.rubber.base.components.config.Locator;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.parser.NacosDataParserHandler;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/1/24
 */
@Getter
@Slf4j
public abstract class BaseRubberConfigLocator implements PropertySourceLocator {


    private NacosConfigManager nacosConfigManager;

    private NacosConfigProperties nacosConfigProperties;

    private RubberProxyConfigProperties rubberProxyConfigProperties;


    public abstract Set<String> createDataIds(Environment environment);


    public abstract String configLocatorName();


    public BaseRubberConfigLocator(NacosConfigManager nacosConfigManager,RubberProxyConfigProperties proxyConfig) {
        this.nacosConfigManager = nacosConfigManager;
        this.nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
        this.rubberProxyConfigProperties = proxyConfig;
    }



    @Override
    public PropertySource<?> locate(Environment environment) {
        try {
            Map<String, Object> configData = initConfigData(environment);
            if (configData == null){
                configData = new HashMap<>(2);
            }
            return new MapPropertySource(configLocatorName(), configData);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }





    private Map<String,Object> initConfigData(Environment environment){
        Set<String> dataIds = createDataIds(environment);
        if (CollUtil.isEmpty(dataIds)){
            return null;
        }
        Map<String,Object> result = new HashMap<>(dataIds.size());
        for (String dataId:dataIds){

            try {
                log.info("rubber config locator dataId={},className={}",dataId,this.getClass().getName());
                String value = nacosConfigManager.getConfigService().getConfig(dataId, nacosConfigProperties.getGroup(), nacosConfigProperties.getTimeout());
                Map<String, Object> p = NacosDataParserHandler.getInstance()
                        .parseNacosData(value, nacosConfigProperties.getFileExtension());
                result.putAll(p);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

}
