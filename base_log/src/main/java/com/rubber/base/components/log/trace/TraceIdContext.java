package com.rubber.base.components.log.trace;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * TraceId 上下文：基于 MDC，在单次请求/线程内串联日志。
 *
 * @author rockyu
 * @date 2021/2/8 17:36
 */
@Component
public class TraceIdContext {

    public static final String TRACE_ID_KEY = "TRACE_ID";

    /** 请求头透传 traceId 的名称 */
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    /**
     * 生成 32 位 hex traceId（UUID 去横线）。
     */
    public static String generate() {
        return IdUtil.simpleUUID();
    }

    public static void setTraceId(String traceId) {
        if (traceId != null && !traceId.isEmpty()) {
            MDC.put(TRACE_ID_KEY, traceId);
        }
    }

    public static String getTraceId() {
        String traceId = MDC.get(TRACE_ID_KEY);
        if (traceId == null) {
            return "";
        }
        return traceId;
    }

    /**
     * 清理当前线程的 traceId，避免线程复用导致串号。
     */
    public static void clear() {
        MDC.remove(TRACE_ID_KEY);
    }
}
