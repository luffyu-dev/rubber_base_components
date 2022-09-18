package com.rubber.basic.web.starter;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author luffyu
 * Created on 2022/4/24
 */
@Configuration
@ComponentScan("com.rubber.**")
@ServletComponentScan
public class RubberBasicWebAuthStarter {

}
