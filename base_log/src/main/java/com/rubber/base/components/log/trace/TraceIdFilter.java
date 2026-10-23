package com.rubber.base.components.log.trace;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 为每个 HTTP 请求注入 traceId 到 MDC，使同一请求内的所有日志可串联。
 * <p>
 * 优先复用上游透传的 {@code X-Trace-Id} 请求头；缺省则生成 32 位 hex。
 * 请求结束（finally）清理 MDC，避免容器线程复用导致串号。
 * 同时回写响应头 {@code X-Trace-Id}，便于上游/前端定位。
 *
 * @author rockyu
 */
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String traceId = request.getHeader(TraceIdContext.TRACE_ID_HEADER);
        if (traceId == null || traceId.isEmpty()) {
            traceId = TraceIdContext.generate();
        }
        MDC.put(TraceIdContext.TRACE_ID_KEY, traceId);
        response.setHeader(TraceIdContext.TRACE_ID_HEADER, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceIdContext.TRACE_ID_KEY);
        }
    }
}
