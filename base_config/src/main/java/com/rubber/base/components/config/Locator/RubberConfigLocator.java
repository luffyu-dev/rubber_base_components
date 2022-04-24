package com.rubber.base.components.config.Locator;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2022/4/24
 */
@Order(1)
public class RubberConfigLocator  extends BaseRubberConfigLocator {


    public RubberConfigLocator(NacosConfigManager nacosConfigManager, RubberProxyConfigProperties proxyConfig) {
        super(nacosConfigManager, proxyConfig);
    }


    @Override
    public Set<String> createDataIds(Environment environment) {
        Set<String> ids = new LinkedHashSet<>();
        ids.add("rubber-basic-config.yml");
        RubberProxyConfigProperties rubberProxyConfig = getRubberProxyConfigProperties();
        if (rubberProxyConfig != null){
            if (StrUtil.isNotEmpty(rubberProxyConfig.getDbInstance())){
                ids.addAll(createDataIds("mysql",rubberProxyConfig.getDbInstance()));
            }
            if (StrUtil.isNotEmpty(rubberProxyConfig.getRedisInstance())){
                ids.addAll(createDataIds("redis",rubberProxyConfig.getRedisInstance()));
            }
        }
        return ids;
    }


    private  Set<String> createDataIds(String type,String configInstances){
        String[] dbInstanceArray = configInstances.split(",");
        Set<String> mysqlDataId = new HashSet<>();
        for (String dbInstance:dbInstanceArray){
            StringBuilder dataId = new StringBuilder(BASE_CONFIG_PREFIX);
            dataId.append(BASE_LINK)
                    .append(type)
                    .append(BASE_LINK)
                    .append(dbInstance)
                    .append(BASE_CONFIG_SUFFIX_FILE_TYPE);
            mysqlDataId.add(dataId.toString());
        }
        //rubber-config-mysql-dev-userDb.yml
        return mysqlDataId;
    }



}
