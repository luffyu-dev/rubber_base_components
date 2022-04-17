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


    /**
     * 获取集群的key指
     * @return 返回一个逗号隔开的集群
     */
    public abstract String getInstanceKey();

    /**
     * 获取资源的名称
     */
    public abstract String configLocatorName();


    public  Set<String> createDataIds(Environment environment){
        String dbInstances = getInstanceKey();
        if (StrUtil.isEmpty(dbInstances)){
            return null;
        }
        String[] dbInstanceArray = dbInstances.split(",");
        Set<String> mysqlDataId = new HashSet<>();
        for (String dbInstance:dbInstanceArray){
            StringBuilder dataId = new StringBuilder(BASE_CONFIG_PREFIX);
            dataId.append(BASE_LINK)
                    .append(configLocatorName())
                    .append(BASE_LINK)
                    .append(dbInstance)
                    .append(BASE_CONFIG_SUFFIX_FILE_TYPE);
            mysqlDataId.add(dataId.toString());
        }
        //rubber-config-mysql-dev-userDb.yml
        return mysqlDataId;
    }


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

    /**
     * 加载配置文件
     */
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
                if (StrUtil.isEmpty(value)){
                    throw new RubberConfigException(" config file " + dataId + "not find");
                }
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
