package com.rubber.base.components.util.session;

import cn.hutool.core.collection.CollectionUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.CompressionAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
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
    public static String createJwtDefault(String subject, long nowTime, long tokenExp, Map<String, Object> userInfo, String secretKey) {
        return createJWT(subject, nowTime, tokenExp, userInfo, secretKey, Jwts.SIG.HS256, Jwts.ZIP.GZIP);
    }


    /**
     * 创建出jwt的token信息
     * @param subject 用户ID 一般是用户的登陆名 或者 唯一id
     * @param nowTime 当前的系统时间戳 毫秒
     * @param tokenExp 时间有效期 单位是毫秒
     * @param userInfo 用户的基本信息 包括角色、权限、名称等信息
     * @param secretKey 加密的key
     * @param algorithm 加密算法（jjwt 0.12 起 SecureDigestAlgorithm，如 Jwts.SIG.HS256）
     * @param compressionCodec 压缩方法（如 Jwts.ZIP.GZIP）
     * @return 返回一个jwt的字符串
     */
    public static String createJWT(String subject, long nowTime, long tokenExp, Map<String, Object> userInfo, String secretKey,
                                   SecureDigestAlgorithm<SecretKey, ?> algorithm, CompressionAlgorithm compressionCodec) {
        JwtBuilder jwtBuilder = Jwts.builder().subject(subject);
        if (CollectionUtil.isNotEmpty(userInfo)) {
            for (Map.Entry<String, Object> map : userInfo.entrySet()) {
                jwtBuilder = jwtBuilder.claim(map.getKey(), map.getValue());
            }
        }
        //设置过期时间
        jwtBuilder.issuedAt(new Date(nowTime))
                .expiration(new Date(nowTime + tokenExp));
        //设置压缩方法
        if (compressionCodec != null) {
            jwtBuilder.compressWith(compressionCodec);
        }
        //设置加密方法（jjwt 0.12 起 signWith 接受 Key + SecureDigestAlgorithm）
        SecretKey key = buildKey(secretKey);
        jwtBuilder.signWith(key, algorithm);
        return jwtBuilder.compact();
    }

    /**
     * 根据 secretKey 字符串构建 SecretKey
     * HS256 要求密钥长度 >= 256bit（32 字节），不足时用 0 填充以保证兼容旧配置
     */
    private static SecretKey buildKey(String secretKey) {
        byte[] bytes = secretKey.getBytes(StandardCharsets.UTF_8);
        byte[] padded = new byte[Math.max(bytes.length, 32)];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        return Keys.hmacShaKeyFor(padded);
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
            SecretKey key = buildKey(secretKey);
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwtToken)
                    .getPayload();
        } catch (ExpiredJwtException e1) {
            //token过期
            throw e1;
        } catch (Exception e) {
            //token错误
            throw e;
        }
    }

}
