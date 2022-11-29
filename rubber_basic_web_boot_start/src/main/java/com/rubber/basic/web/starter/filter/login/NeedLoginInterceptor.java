package com.rubber.basic.web.starter.filter.login;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rubber.base.components.util.annotation.NeedLogin;
import com.rubber.base.components.util.result.code.SysCode;
import com.rubber.base.components.util.result.exception.BaseResultRunTimeException;
import com.rubber.base.components.util.session.BaseUserSession;
import com.rubber.base.components.util.session.JwtSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author luffyu
 * Created on 2022/9/17
 */
@Slf4j
public class NeedLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        NeedLogin needLogin = handlerMethod.getMethodAnnotation(NeedLogin.class);
        if (needLogin == null){
            return true;
        }
        // 从request的请求中解析出用户的登录态
        BaseUserSession baseUserSession = checkAndCreateBaseUserSession(request,needLogin);
        if (needLogin.request() && baseUserSession == null){
            throw new BaseResultRunTimeException(SysCode.LOGIN_EXPIRED);
        }
        // 把用户信息写到body中
        if(!setUserSessionToBody(request, handlerMethod,baseUserSession)){
            throw new BaseResultRunTimeException(SysCode.LOGIN_EXPIRED);
        }
        return true;
    }

    /**
     * 设置用户信息到body中
     * @param request
     * @param handlerMethod
     * @param baseUserSession
     * @return
     */
    private boolean setUserSessionToBody(HttpServletRequest request, HandlerMethod handlerMethod, BaseUserSession baseUserSession){
        try{
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            if(ArrayUtil.isEmpty(methodParameters)) {
                return false;
            }
            for (MethodParameter methodParameter : methodParameters) {
                Class clazz = methodParameter.getParameterType();
                if(ClassUtils.isAssignable(BaseUserSession.class, clazz)){
                    if(request instanceof CustomHttpServletRequestWrapper){
                        CustomHttpServletRequestWrapper requestWrapper = (CustomHttpServletRequestWrapper)request;
                        String body = requestWrapper.getBody();
                        JSONObject param ;
                        if (StrUtil.isNotEmpty(body) && body.startsWith("{") && body.endsWith("}")){
                            param = JSONObject.parseObject(body);
                        }else {
                            param = new JSONObject();
                        }
                        if (baseUserSession != null){
                            baseUserSession.setSysReqTime(new Date());
                            param.putAll(JSONObject.parseObject(JSON.toJSONString(baseUserSession)));
                        }else {
                            param.remove("uid");
                            param.remove("v");
                            param.remove("name");
                            param.put("sysReqTime",new Date());
                        }
                        requestWrapper.setBody(JSONObject.toJSONString(param));
                        return true;
                    }
                }
            }
            return false;
        }catch (Exception e){
            log.warn("fill userInfo to request body Error ", e);
            return false;
        }
    }



    private BaseUserSession checkAndCreateBaseUserSession(HttpServletRequest request,NeedLogin needLogin){
        Cookie[] cookieArray = request.getCookies();
        if (ArrayUtil.isEmpty(cookieArray)){
            return null;
        }
        String cookieValue = "";
        for (Cookie cookie:cookieArray){
            if (cookie != null && "Login-Token".equals(cookie.getName())){
                cookieValue = cookie.getValue();
                break;
            }
        }
        if (StrUtil.isEmpty(cookieValue)){
            return null;
        }
        try{
            // value的解析
            return JwtSessionUtil.checkSession(cookieValue);
        }catch (Exception e){
            log.error("解析登录态失败，value={}",cookieValue);
            return null;
        }
    }



}
