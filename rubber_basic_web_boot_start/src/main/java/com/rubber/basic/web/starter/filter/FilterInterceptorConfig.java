package com.rubber.basic.web.starter.filter;

import com.rubber.basic.web.starter.filter.login.CustomSessionFilter;
import com.rubber.basic.web.starter.filter.login.NeedLoginInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author luffyu
 * Created on 2022/9/17
 */
@Configuration
public class FilterInterceptorConfig implements WebMvcConfigurer {



    @Bean
    public FilterRegistrationBean servletRegistrationBean() {
        CustomSessionFilter userInfoFilter = new CustomSessionFilter();
        FilterRegistrationBean<CustomSessionFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(userInfoFilter);
        bean.setName("userInfoFilter");
        bean.addUrlPatterns("/*");
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return bean;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns用于添加拦截路径
        //excludePathPatterns用于添加不拦截的路径
        registry.addInterceptor(new NeedLoginInterceptor());
    }
}
