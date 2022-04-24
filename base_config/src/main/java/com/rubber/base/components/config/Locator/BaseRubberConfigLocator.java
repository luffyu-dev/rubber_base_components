package com.rubber.base.components.config.Locator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.parser.NacosDataParserHandler;
import com.rubber.base.components.config.exception.RubberConfigException;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/1/24
 */
@Getter
@Slf4j
public abstract class BaseRubberConfigLocator implements PropertySourceLocator {

    /**
     * 通用的配置前缀
     */
    protected static final String BASE_CONFIG_PREFIX = "rubber-config";

    protected static final String BASE_LINK = "-";
    /**
     * 通用的后缀名称
     */
    protected static final String BASE_CONFIG_SUFFIX_FILE_TYPE = ".yml";


    private NacosConfigManager nacosConfigManager;

    private NacosConfigProperties nacosConfigProperties;

    private RubberProxyConfigProperties rubberProxyConfigProperties;


    public  abstract Set<String> createDataIds(Environment environment);


    public BaseRubberConfigLocator(NacosConfigManager nacosConfigManager,RubberProxyConfigProperties proxyConfig) {
        this.nacosConfigManager = nacosConfigManager;
        this.nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
        this.rubberProxyConfigProperties = proxyConfig;
    }



    @Override
    public PropertySource<?> locate(Environment environment) {
        try {
            Set<String> dataIds = createDataIds(environment);
            log.info("BaseRubberConfigLocator start get config ids = {}",dataIds);
            if (CollUtil.isEmpty(dataIds)){
                return null;
            }
            Map<String, Object> configData = initConfigData(dataIds);
            return new MapPropertySource("rubber", configData);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    /**
     * 加载配置文件
     */
    private Map<String,Object> initConfigData(Set<String> dataIds){
        Map<String,Object> result = new HashMap<>(dataIds.size());
        for (String dataId:dataIds){
            result.putAll(getConfigData(dataId));
        }
        return result;
    }


    /**
     * 加载配置文件
     */
    private Map<String,Object> getConfigData(String dataId){
        Map<String,Object> result = new HashMap<>(16);
        try {
            log.info("rubber config locator dataId={},className={}",dataId,this.getClass().getName());
            String value = nacosConfigManager.getConfigService().getConfig(dataId, nacosConfigProperties.getGroup(), nacosConfigProperties.getTimeout());
            if (StrUtil.isEmpty(value)){
                throw new RubberConfigException(" config file " + dataId + "not find");
            }
            Map<String, Object> p = NacosDataParserHandler.getInstance()
                    .parseNacosData(value, nacosConfigProperties.getFileExtension());
            result.putAll(p);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
