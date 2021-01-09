package com.rubber.base.components.config;

import com.alibaba.cloud.nacos.NacosConfigManager;
import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * @author luffyu
 * Created on 2020/12/13
 */
@Data
public class RubberConfiguration {

    @Bean
    public RubberPropertySourceLocator myPropertySourceLocator(NacosConfigManager nacosConfigManager){
        return new RubberPropertySourceLocator(nacosConfigManager);
    }
}
