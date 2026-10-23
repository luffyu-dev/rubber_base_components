package com.rubber.base.components.log;

import com.rubber.base.components.log.trace.TraceIdFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * base_log 自动装配：依赖本模块的 web 工程开箱即用。
 * <ul>
 *     <li>注册 {@link TraceIdFilter}（最高优先级，早于业务 Filter），为每个请求注入 traceId。</li>
 *     <li>logback.xml 通过 classpath:config/application.yml 自动加载，无需额外声明。</li>
 * </ul>
 * 仅在 Servlet web 环境下生效；非 web 工程不注册 Filter，日志切分配置仍可通过 logback.xml 生效。
 *
 * @author rockyu
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Filter.class, FilterRegistrationBean.class})
public class RubberLogAutoConfiguration {

    /**
     * 业务方可通过自定义同名 Bean 覆盖 Filter 行为。
     */
    @Bean
    @ConditionalOnMissingBean(TraceIdFilter.class)
    public TraceIdFilter rubberTraceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    @ConditionalOnMissingBean(name = "rubberTraceIdFilterRegistration")
    public FilterRegistrationBean<TraceIdFilter> rubberTraceIdFilterRegistration(TraceIdFilter traceIdFilter) {
        FilterRegistrationBean<TraceIdFilter> registration = new FilterRegistrationBean<>(traceIdFilter);
        registration.addUrlPatterns("/*");
        registration.setName("rubberTraceIdFilter");
        // 最早执行，保证后续 Filter/Controller 的日志都带 traceId
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
        return registration;
    }
}
