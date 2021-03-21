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
@Order(2)
public class RedisRubberConfigLocator extends BaseRubberConfigLocator {


    public RedisRubberConfigLocator(NacosConfigManager nacosConfigManager, RubberProxyConfigProperties rubberProxyConfigProperties) {
        super(nacosConfigManager,rubberProxyConfigProperties);
    }

    @Override
    public Set<String> doCreateDataIds(Environment environment,String[] activeProfiles) {
        String redisInstances = getRubberProxyConfigProperties().getRedisInstance();
        if (StrUtil.isEmpty(redisInstances)){
            return null;
        }
        String[] redisInstanceNameArray = redisInstances.split(",");

        Set<String> redisDataId = new HashSet<>();
        for (String activeProfile:activeProfiles){
            StringBuilder dataId = new StringBuilder(activeProfile+"-rubber-config-redis-");
            for (String redisInstance:redisInstanceNameArray){
                dataId.append(redisInstance).append(".yml");
            }
            redisDataId.add(dataId.toString());
        }
        //rubber-config-mysql-dev-userDb.yml
        return redisDataId;
    }

    @Override
    public String configLocatorName() {
        return "rubberConfigRedis";
    }


}
