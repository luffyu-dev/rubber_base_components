package com.rubber.base.components.config.config;

import com.rubber.base.components.config.proxy.ProxyRestLocalTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
@Configuration
public class RubberProxyConfig {


    @Bean
    public ProxyRestLocalTemplate proxyRestLocalTemplate(){
        return new ProxyRestLocalTemplate();
    }

}
