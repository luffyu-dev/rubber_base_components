package com.rubber.base.components.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.rubber.base.components.config.Locator.MySqlRubberConfigLocator;
import com.rubber.base.components.config.Locator.RedisRubberConfigLocator;
import com.rubber.base.components.config.properties.RubberProxyConfigProperties;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author luffyu
 * Created on 2020/12/13
 */
@Data
public class RubberConfiguration {



    @Bean
    @ConditionalOnMissingBean
    public RubberProxyConfigProperties rubberProxyConfigProperties(){
        return new RubberProxyConfigProperties();
    }



    @Bean
    public MySqlRubberConfigLocator mysqlPropertySourceLocator( NacosConfigManager nacosConfigManager,RubberProxyConfigProperties rubberProxyConfigProperties){
        return new MySqlRubberConfigLocator(nacosConfigManager,rubberProxyConfigProperties);
    }


    @Bean
    public RedisRubberConfigLocator redisPropertySourceLocator(NacosConfigManager nacosConfigManager,RubberProxyConfigProperties rubberProxyConfigProperties){
        return new RedisRubberConfigLocator(nacosConfigManager,rubberProxyConfigProperties);
    }
}
