package com.rubber.base.components.config;

import cn.hutool.core.map.MapUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.parser.NacosDataParserHandler;
import lombok.Data;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2020/12/13
 */
@Order(1)
@Data
public class RubberPropertySourceLocator implements PropertySourceLocator {


    private NacosConfigProperties nacosConfigProperties;

    private NacosConfigManager nacosConfigManager;


    public RubberPropertySourceLocator(NacosConfigManager nacosConfigManager) {
        this.nacosConfigManager = nacosConfigManager;
        this.nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
    }


    @Override
    public PropertySource<?> locate(Environment environment) {
        try {
            Map<String, Object> p = initConfigData("dev",environment);
            return new MapPropertySource("DEFAULT_GROUP,test.yml", p);
        }catch (Exception e){
            throw new RuntimeException();
        }
    }



    private Map<String,Object> initConfigData(String projectActive,Environment environment){
        Map<String,String> dataIdMap = createDataId(projectActive,environment);
        if (MapUtil.isEmpty(dataIdMap)){
            return null;
        }

        Map<String,Object> result = new HashMap<>();
        for (Map.Entry<String,String> dataId:dataIdMap.entrySet()){

            try {
                String value = nacosConfigManager.getConfigService().getConfig(dataId.getKey(), dataId.getValue(), 5000);
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
        Map<String,String> dataIds = new HashMap<>(10);
        String dbDataId = dataIdPrefix + "mysql-" + projectActive + "-userDb.yml";
        dataIds.putIfAbsent(dbDataId,group);

        return dataIds;
    }




}
