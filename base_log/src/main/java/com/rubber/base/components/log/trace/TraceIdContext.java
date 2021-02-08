package com.rubber.base.components.log.trace;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * @author rockyu
 * @date 2021/2/8 17:36
 */
@Component
public class TraceIdContext {

    public static final String TRACE_ID_KEY = "TRACE_ID";

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
}
