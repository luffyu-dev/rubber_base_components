package com.rubber.basic.web.starter.filter.login;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rubber.base.components.util.annotation.NeedLogin;
import com.rubber.base.components.util.result.RubberSystem;
import com.rubber.base.components.util.result.code.SysCode;
import com.rubber.base.components.util.result.exception.BaseResultRunTimeException;
import com.rubber.base.components.util.session.BaseUserSession;
import com.rubber.base.components.util.session.JwtSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @author luffyu
 * Created on 2022/9/17
 */
@Slf4j
public class NeedLoginInterceptor implements HandlerInterceptor {


    /**
     * 入参处理
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 替换 HttpServletResponse
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        BaseUserSession baseReq  = new BaseUserSession();
        baseReq.setTraceId("T"+ IdUtil.fastSimpleUUID());
        baseReq.setUid(null);
        baseReq.setName(null);
        baseReq.setRole(null);

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        NeedLogin needLogin = handlerMethod.getMethodAnnotation(NeedLogin.class);
        if (needLogin != null){
            // 从request的请求中解析出用户的登录态
            BaseUserSession loginSession = checkAndCreateBaseUserSession(request);
            if (needLogin.request() && loginSession == null){
                throw new BaseResultRunTimeException(SysCode.LOGIN_EXPIRED);
            }
            if (loginSession != null){
                baseReq.setUid(loginSession.getUid());
                baseReq.setName(loginSession.getName());
                baseReq.setRole(loginSession.getRole());
            }
        }
        // 把用户信息写到body中
//        if(!setUserSessionToBody(request, handlerMethod,baseReq)){
//            throw new BaseResultRunTimeException(SysCode.LOGIN_EXPIRED);
//        }
        trySetUserSessionToBody(request, handlerMethod,baseReq);
        return true;
    }


    /**
     * 返回值处理
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws IOException {

        BaseUserSession baseUserSession = null;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NeedLogin needLogin = handlerMethod.getMethodAnnotation(NeedLogin.class);

            if (needLogin != null && request instanceof CustomHttpServletRequestWrapper) {
                CustomHttpServletRequestWrapper requestWrapper = (CustomHttpServletRequestWrapper) request;
                String body = requestWrapper.getBody();
                if (StrUtil.isNotEmpty(body) && body.startsWith("{")) {
                    baseUserSession = JSON.parseObject(body,BaseUserSession.class);
                }
            }
        }



        if (response instanceof CustomResponseWrapper){
            CustomResponseWrapper responseWrapper = (CustomResponseWrapper) response;
            // 获取原始响应内容
            String originalContent = responseWrapper.getCapturedResponse();
            if (StrUtil.isNotEmpty(originalContent) && originalContent.startsWith("{")){
                JSONObject resContent = JSON.parseObject(originalContent);


                RubberSystem rubberSystem = new RubberSystem();
                rubberSystem.setIp("");
                rubberSystem.setTraceId("T"+ IdUtil.fastSimpleUUID());

                if (baseUserSession != null){
                    rubberSystem.setTraceId(baseUserSession.getTraceId());
                    rubberSystem.setUid(baseUserSession.getUid());
                    rubberSystem.setUserRole(baseUserSession.getRole());
                    rubberSystem.setUserName(baseUserSession.getName());
                }

                resContent.put("sysData",rubberSystem);
                originalContent = JSON.toJSONString(resContent);
            }
            responseWrapper.writeModifiedResponse(originalContent);
        }

    }




    /**
     * 设置用户信息到body中
     * @param request
     * @param handlerMethod
     * @param baseUserSession
     * @return
     */
    private boolean trySetUserSessionToBody(HttpServletRequest request, HandlerMethod handlerMethod, BaseUserSession baseUserSession){
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
                            param.remove("appVersion");
                            param.remove("role");
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



    private BaseUserSession checkAndCreateBaseUserSession(HttpServletRequest request){
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
