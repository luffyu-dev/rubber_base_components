package com.rubber.basic.web.starter.filter.login;



import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
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
        chain.doFilter((Objects.isNull(customHttpServletRequestWrapper) ? request : customHttpServletRequestWrapper), response);
    }
}