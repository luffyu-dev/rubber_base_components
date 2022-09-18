package com.rubber.base.components.util.session;


import com.alibaba.fastjson.JSON;
import com.rubber.base.components.util.result.code.SysCode;
import com.rubber.base.components.util.result.exception.BaseResultRunTimeException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;


/**
 * @author luffyu
 * Created on 2022/9/18
 */
@Slf4j
public class JwtSessionUtil {

    private static final String SECRET_KEY = "NIoVjZPMsZC";

    /**
     * @param sessionKey
     * @return
     */
    public static BaseUserSession checkSession(String sessionKey) {
        try {
            Claims claims = JwtUtil.checkToken(sessionKey, SECRET_KEY);
            //登陆名称
            String subject = claims.getSubject();
            BaseUserSession baseUserSession = JSON.parseObject(JSON.toJSONString(claims),BaseUserSession.class);
            log.info("当前验证登录态成功 subject={},session={}",subject,baseUserSession);
            return baseUserSession;
        }catch (JwtException e){
            throw new BaseResultRunTimeException(SysCode.LOGIN_EXPIRED);
        } catch (Throwable e) {
            log.error("登录出现了异常信息-msg={}",e.getMessage());
            throw new BaseResultRunTimeException(SysCode.LOGIN_EXPIRED);
        }
    }

}
