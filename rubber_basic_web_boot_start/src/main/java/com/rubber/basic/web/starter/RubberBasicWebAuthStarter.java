package com.rubber.basic.web.starter;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author luffyu
 * Created on 2022/4/24
 */
@EnableDubbo(scanBasePackages = "com.rubber.**")
@Configuration
@ComponentScan("com.rubber.**")
@ServletComponentScan
public class RubberBasicWebAuthStarter {

}
