package com.rubber.basic.web.starter.filter.login;



import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author luffyu
 * Created on 2022/9/12
 */
public class CustomSessionFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        CustomHttpServletRequestWrapper customHttpServletRequestWrapper = null;
        try {
            HttpServletRequest req = (HttpServletRequest)request;
            customHttpServletRequestWrapper = new CustomHttpServletRequestWrapper(req);
        }catch (Exception e){

        }


        CustomResponseWrapper customHttpServletResponseWrapper = null;
        // SSE（text/event-stream）响应必须每个事件实时写回并 flush 到客户端；
        // ContentCachingResponseWrapper 会把整个响应体缓存在内存里，导致流式响应失效，因此跳过包装
        if (!isSseRequest(request)) {
            try {
                HttpServletResponse res = (HttpServletResponse)response;
                customHttpServletResponseWrapper = new CustomResponseWrapper(res);
            }catch (Exception e){

            }
        }


        try {
            chain.doFilter(
                    (Objects.isNull(customHttpServletRequestWrapper) ? request : customHttpServletRequestWrapper),
                    (Objects.isNull(customHttpServletResponseWrapper) ? response : customHttpServletResponseWrapper)
            );
        }finally {
            copyBodyToResponse(customHttpServletResponseWrapper);
        }
    }

    private void copyBodyToResponse(CustomResponseWrapper responseWrapper) throws IOException {
        if (responseWrapper == null){
            return;
        }
        if (responseWrapper.isCommitted()){
            return;
        }
        byte[] body = responseWrapper.getContentAsByteArray();
        if (body.length > 0) {
            responseWrapper.copyBodyToResponse();
        }
    }

    private boolean isSseRequest(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            return false;
        }
        String accept = ((HttpServletRequest) request).getHeader("Accept");
        return accept != null && accept.contains("text/event-stream");
    }
}
