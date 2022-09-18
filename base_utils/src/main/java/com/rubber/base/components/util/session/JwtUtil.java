package com.rubber.base.components.util.session;

import cn.hutool.core.collection.CollectionUtil;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

/**
 * @author luffyu
 * Created on 2021/9/3
 */
public class JwtUtil {


    /**
     * 创建出jwt的token信息
     * @param subject 用户ID 一般是用户的登陆名 或者 唯一id
     * @param nowTime 当前的系统时间戳 毫秒
     * @param tokenExp 时间有效期 单位是毫秒
     * @param userInfo 用户的基本信息 包括角色、权限、名称等信息
     * @param secretKey 加密的key
     * @return 返回一个jwt的字符串
     */
    public static String createJwtDefault(String subject,long nowTime,long tokenExp,Map<String,Object> userInfo,String secretKey){
        return createJWT(subject,nowTime,tokenExp,userInfo,secretKey, SignatureAlgorithm.HS256, CompressionCodecs.GZIP);
    }



    /**
     * 创建出jwt的token信息
     * @param subject 用户ID 一般是用户的登陆名 或者 唯一id
     * @param nowTime 当前的系统时间戳 毫秒
     * @param tokenExp 时间有效期 单位是毫秒
     * @param userInfo 用户的基本信息 包括角色、权限、名称等信息
     * @param secretKey 加密的key
     * @param algorithm 加密算法
     * @param compressionCodec 压缩方法
     * @return 返回一个jwt的字符串
     */
    public static String createJWT(String subject, long nowTime, long tokenExp, Map<String,Object> userInfo, String secretKey, SignatureAlgorithm algorithm, CompressionCodec compressionCodec){
        JwtBuilder jwtBuilder = Jwts.builder().setSubject(subject);
        if(CollectionUtil.isNotEmpty(userInfo)){
            for(Map.Entry<String,Object> map:userInfo.entrySet()){
                jwtBuilder = jwtBuilder.claim(map.getKey(),map.getValue());
            }
        }
        //设置过期时间
        jwtBuilder.setIssuedAt(new Date(nowTime))
                .setExpiration(new Date(nowTime + tokenExp));
        //设置压缩方法
        if(compressionCodec != null ){
            jwtBuilder.compressWith(compressionCodec);
        }
        //设置加密方法
        jwtBuilder.signWith(algorithm, secretKey);
        return jwtBuilder.compact();
    }



    /**
     * token验证是否正确
     * @param jwtToken  jwt的加密token信息
     * @param secretKey 加密的key
     * @return 返回key
     * @throws Exception 异常信息
     */
    public static Claims checkToken(String jwtToken, String secretKey) throws Exception {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken).getBody();
        } catch (ExpiredJwtException e1) {
            //token过期
            throw e1;
        } catch (Exception e) {
            //token错误
            throw e;
        }
    }

}
