package com.rubber.base.components.config.proxy;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/3/21
 */
public class ProxyRestLocalTemplate extends RestTemplate {

    public ProxyRestLocalTemplate() {
    }

    public ProxyRestLocalTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
    }

    public ProxyRestLocalTemplate(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
        return super.getForObject(url, responseType, uriVariables);
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
        return super.getForObject(url, responseType, uriVariables);
    }

    @Override
    public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
        return super.getForObject(url, responseType);
    }
}
