package com.rubber.base.components.tools;

import com.rubber.common.utils.enums.EnvEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author luffyu
 * Created on 2020/10/25
 */
@Slf4j
@Component
public class SpringTools implements ApplicationListener<ContextRefreshedEvent> {


    /**
     * 当前的applicationContext 对象
     */
    static ApplicationContext applicationContext;

    /**
     * 当前环境的key值信息
     */
    static String envKey;



    private SpringTools() {
    }


    /**
     * @return 返回当前的applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @return 返回当前的环境key值信息
     */
    public static String getEnvKey() {
        return envKey;
    }




    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        applicationContext = contextRefreshedEvent.getApplicationContext();
        //获取当前的环境对象
        envKey = getEnv(applicationContext.getEnvironment());
    }


    /**
     * 获取当前的环境字段信息
     * @param environment 当前的环境配置
     * @return 环境信息
     */
    private String getEnv(Environment environment){
        if (environment == null){
            return null;
        }
        //获取当前的环境配置信息
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length > 0){
            return profiles[0];
        }
        return null;
    }
}
