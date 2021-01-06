package com.rubber.base.components.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.parser.NacosDataParserHandler;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.rubber.base.components.config.exception.RubberConfigException;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.*;

/**
 * @author luffyu
 * Created on 2020/12/13
 */
@Order(1)
public class RubberEnvironmentPostProcessor implements PropertySourceLocator {



    public ComponentsConfigProperties componentsConfigProperties;


    public RubberEnvironmentPostProcessor(ComponentsConfigProperties componentsConfigProperties) {
        this.componentsConfigProperties = componentsConfigProperties;
    }



    @Override
    public PropertySource<?> locate(Environment environment) {
        try {
            String nacosServerAdd = "127.0.0.1:8848";
            if (StrUtil.isEmpty(nacosServerAdd)){
                throw new RubberConfigException("spring.cloud.nacos.server-addr is null");
            }
            Properties properties = new Properties();
            properties.put("serverAddr", nacosServerAdd);
            ConfigService configService = NacosFactory.createConfigService(properties);
            Map<String, Object> p = initConfigData(configService,"dev",environment);
            return new MapPropertySource("DEFAULT_GROUP,test.yml", p);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }



    private Map<String,Object> initConfigData(ConfigService configService,String projectActive,Environment environment){
        Map<String,String> dataIdMap = createDataId(projectActive,environment);
        if (MapUtil.isEmpty(dataIdMap)){
            return null;
        }

        Map<String,Object> result = new HashMap<>();
        for (Map.Entry<String,String> dataId:dataIdMap.entrySet()){

            try {
                String value = configService.getConfig(dataId.getKey(), dataId.getValue(), 5000);
                Map<String, Object> p = NacosDataParserHandler.getInstance()
                        .parseNacosData(value, "yml");
                result.putAll(p);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }





    private Map<String,String> createDataId(String projectActive,Environment environment){
        String dataIdPrefix = "rubber-config-";
        String group = "DEFAULT_GROUP";
        String dbSet = environment.getProperty("rubber.components.config.dbSet");
        Map<String,String> dataIds = new HashMap<>(10);
        String dbDataId = dataIdPrefix + "mysql-" + projectActive + "-userDb.yml";
        dataIds.putIfAbsent(dbDataId,group);

        return dataIds;
    }




}
