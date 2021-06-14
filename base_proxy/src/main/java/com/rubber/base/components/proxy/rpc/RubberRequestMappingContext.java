package com.rubber.base.components.proxy.rpc;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author luffyu
 * Created on 2021/6/1
 */
@Component
public class   RubberRequestMappingContext implements ApplicationListener<ContextRefreshedEvent> {

    private Map<String,HandlerMethod> urlMappingDict  = new HashMap<>();

    /**
     * url的比较方法
     */
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private ApplicationContext applicationContext;

    public Object getBean(String name){
        return applicationContext.getBean(name);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        this.applicationContext = applicationContext;
        RequestMappingHandlerMapping bean = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();

        if(MapUtil.isNotEmpty(handlerMethods)){

            for(Map.Entry<RequestMappingInfo, HandlerMethod> map:handlerMethods.entrySet()){
                //获取到请求到url
                PatternsRequestCondition pc = map.getKey().getPatternsCondition();
                Set<String> pSet = pc.getPatterns();
                if (CollectionUtil.isEmpty(pSet)){
                    continue;
                }
                //处理生成requestOriginBean
                urlMappingDict.put(pSet.iterator().next(),map.getValue());
            }
        }
    }


    /**
     * 判断是否可以通过url读取到本地的接口请求
     * @param url 当前的url
     * @return 返回方法对象
     */
    public HandlerMethod getByUrl(String url){
        for (Map.Entry<String,HandlerMethod> data: urlMappingDict.entrySet()){
            String key = data.getKey();
            if (antPathMatcher.match(key,url)){
                return data.getValue();
            }
        }
        return null;
    }
}
