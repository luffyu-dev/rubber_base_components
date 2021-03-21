package com.rubber.base.components.config.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
@Component
public class HttpRequestOriginProvider implements ApplicationContextAware {



    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        initRequestOriginBean(applicationContext);
    }

    /**
     * 初始化当前的MappingBean方法
     * @param applicationContext spring上下文
     */
    private void initRequestOriginBean(ApplicationContext applicationContext){
        //获取全部的RequestMapping映射器
        RequestMappingHandlerMapping bean = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取到全部到Mapping信息
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();
        if(MapUtil.isNotEmpty(handlerMethods)){

            for(Map.Entry<RequestMappingInfo, HandlerMethod> map:handlerMethods.entrySet()){
                //获取到请求到url
                PatternsRequestCondition pc = map.getKey().getPatternsCondition();
                Set<String> pSet = pc.getPatterns();
                if (CollectionUtil.isEmpty(pSet)){
                    continue;
                }
                String url = pSet.iterator().next();
                HandlerMethod handlerMethod = map.getValue();
            }
        }
    }
}
