package com.rubber.basic.service.starter;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author luffyu
 * Created on 2022/4/24
 */
@EnableDubbo
@Configuration
@ComponentScan("com.rubber.**")
public class RubberBasicServiceAuthStarter {

}
