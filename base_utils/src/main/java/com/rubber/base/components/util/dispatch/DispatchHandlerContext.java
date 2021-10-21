package com.rubber.base.components.util.dispatch;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.rubber.base.components.util.dispatch.bean.DispatchAble;
import com.rubber.base.components.util.dispatch.bean.DispatchConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luffyu
 * Created on 2021/10/21
 */
@Slf4j
@Component
public class DispatchHandlerContext implements ApplicationListener<ContextRefreshedEvent> {


    /**
     * 分发器的容器
     */
    Map<Class<?>, Map<String,Object> > dispatchServiceContainer = new ConcurrentHashMap<>(32);




    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        log.info("开始加载Service分发器");
        Map<String, DispatchAble> beansOfType = event.getApplicationContext().getBeansOfType(DispatchAble.class);

        if (CollectionUtil.isNotEmpty(beansOfType)){
            for (Map.Entry<String, DispatchAble> map:beansOfType.entrySet()){
                String key = map.getKey();
                DispatchAble service = map.getValue();
                DispatchConfig annotation = service.getClass().getAnnotation(DispatchConfig.class);
                if (annotation == null){
                    log.error("加载Service分发器——类{}实现了DispatchAble但是没有配置DispatchConfig",service.getClass().getName());
                    continue;
                }
                if (StrUtil.isNotEmpty(annotation.key())){
                    key = annotation.key();
                }
                Class<?> type = annotation.type();
                if (!type.isAssignableFrom(service.getClass())){
                    log.error("加载Service分发器——类{}没有实现DispatchConfig配置的type{}",service.getClass().getName(),type.getName());
                    continue;
                }
                Map<String, Object> stringClassMap = dispatchServiceContainer.computeIfAbsent(type, i -> new HashMap<>(32));
                stringClassMap.putIfAbsent(key,service);
                log.info("加载Service分发器——成功加载类{}到{}的分发组，索引Key为{}",service.getClass().getName(),type.getName(),key);
            }
        }
        log.info("完成加载Service分发器");
    }


    /**
     * 分发方法
     * @param tClass 当前类型
     * @param key 索引key值
     * @param <T> 类型
     * @return
     */
    public <T> T dispatch(Class<T> tClass,String key){
        Map<String, Object> serviceContainer = dispatchServiceContainer.get(tClass);
        if (serviceContainer == null){
            log.error("不存在类为{}的的服务",tClass);
            return null;
        }
        Object o = serviceContainer.get(key);
        if (o == null){
            log.error("不存在类为{}的KEY{}的服务",tClass,key);
            return null;
        }
        return (T)o;
    }
}
