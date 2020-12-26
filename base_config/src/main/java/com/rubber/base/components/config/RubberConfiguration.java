package com.rubber.base.components.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author luffyu
 * Created on 2020/12/13
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "rubber.components")
public class RubberConfiguration {


    private ComponentsConfigProperties config;



    @Bean
    public RubberEnvironmentPostProcessor myPropertySourceLocator(){
        return new RubberEnvironmentPostProcessor(config);
    }
}
