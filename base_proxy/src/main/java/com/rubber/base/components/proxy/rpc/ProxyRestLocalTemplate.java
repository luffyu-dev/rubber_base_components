package com.rubber.base.components.proxy.rpc;

import cn.hutool.core.util.URLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
@Slf4j
public class ProxyRestLocalTemplate extends RestTemplate {

    /**
     * 当前jvm中的handlerMapping映射对象
     */
    RubberRequestMappingContext rubberRequestMappingContext;


    public ProxyRestLocalTemplate(RubberRequestMappingContext rubberRequestMappingContext){
        this.rubberRequestMappingContext = rubberRequestMappingContext;
    }

    /**
     * {@inheritDoc}
     * <p>To provide a {@code RequestCallback} or {@code ResponseExtractor} only,
     * but not both, consider using:
     * <ul>
     * <li>{@link #acceptHeaderRequestCallback(Class)}
     * <li>{@link #httpEntityCallback(Object)}
     * <li>{@link #httpEntityCallback(Object, Type)}
     * <li>{@link #responseEntityExtractor(Type)}
     * </ul>
     *
     * @param url
     * @param method
     * @param requestCallback
     * @param responseExtractor
     * @param uriVariables
     */
    @Override
    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) throws RestClientException {
        Object proxyResult = doProxyNowRequestMapping(url, uriVariables);
        if (proxyResult != null){
            return (T)proxyResult;
        }
        return super.execute(url, method, requestCallback, responseExtractor, uriVariables);
    }


    /**
     * {@inheritDoc}
     * <p>To provide a {@code RequestCallback} or {@code ResponseExtractor} only,
     * but not both, consider using:
     * <ul>
     * <li>{@link #acceptHeaderRequestCallback(Class)}
     * <li>{@link #httpEntityCallback(Object)}
     * <li>{@link #httpEntityCallback(Object, Type)}
     * <li>{@link #responseEntityExtractor(Type)}
     * </ul>
     */
    @Override
    @Nullable
    public <T> T execute(String url, HttpMethod method, @Nullable RequestCallback requestCallback,
                         @Nullable ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException {
        Object proxyResult = doProxyNowRequestMapping(url, uriVariables);
        if (proxyResult != null){
            return (T)proxyResult;
        }
        URI expanded = getUriTemplateHandler().expand(url, uriVariables);
        return doExecute(expanded, method, requestCallback, responseExtractor);
    }



    private Object doProxyNowRequestMapping(String url,Object... uriVariables){
        String path = URLUtil.getPath(url);
        HandlerMethod handlerMethod = rubberRequestMappingContext.getByUrl(path);
        if (handlerMethod != null){
            try {
                Object bean = rubberRequestMappingContext.getBean(handlerMethod.getBean().toString());
                return handlerMethod.getMethod().invoke(bean, uriVariables);
            }catch (Exception e){
                log.info("当前代理请求的参数异常，result = {}",e.getMessage());
                return null;
            }

        }
        return null;
    }
}
