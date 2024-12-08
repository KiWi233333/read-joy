package com.readjoy.readjoyapi.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * JWt工具类
 *
 * @className: JWTUtil
 * @author: Kiwi2333
 * @description: TODO描述
 * @date: 2023/4/13 1:17
 */
@Slf4j
@Component
public class JWTUtil {

    static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 1、生成Token
     *
     * @param obj 加密对象
     * @return token
     */
    public static String createToken(UserTokenUtil obj) {
        try {
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("alg", "HS256");
            header.put("Type", "Jwt");
            Date now = new Date();
            String userJson = objectMapper.writeValueAsString(obj);
            return JWT.create().withHeader(header).withNotBefore(now) // 生效时间
                    .withIssuedAt(now) // 签发时间
                    .withIssuer(JwtConstant.ISSUER) // 用于说明该JWT是由谁签发的
                    .withSubject(JwtConstant.SUBJECT_OBJ) // 用于说明该JWT面向的对象
                    .withAudience(JwtConstant.SAVE_OBJ_KEY) // 用于说明该JWT发送给的用户
                    // 有效期
                    .withExpiresAt(Date.from(ZonedDateTime.now().plusMinutes(JwtConstant.TOKEN_TIME).toInstant())) // 数字类型，说明该JWT过期的时间  //ZonedDateTime.now().plusMinutes()此方法基于此日期时间返回添加了分钟数的ZonedDateTime    // 随机jwtId
                    .withJWTId(UUID.randomUUID().toString()) // 说明标明JWT的唯一ID
                    // 存储地址
                    .withClaim(JwtConstant.SAVE_OBJ_KEY, userJson) // 存入user信息
                    // 算法
                    .sign(Algorithm.HMAC256(JwtConstant.SECRET_KEY));
        } catch (Exception e) {
            log.error("生成token失败！\n {}", e.getMessage());
            return null;
        }
    }

    /**
     * 检验token是否正确
     *
     * @param token token
     * @return JsonParser
     */
    public static JsonParser checkToken(String token) throws IOException {
        Algorithm algorithm = Algorithm.HMAC256(JwtConstant.SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(JwtConstant.ISSUER) // 用于说明该JWT是由谁签发的
                .withSubject(JwtConstant.SUBJECT_OBJ) // 用于说明该JWT面向的对象
                .withAudience(JwtConstant.SAVE_OBJ_KEY) // 用于说明该JWT发送给的用户
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String tokenInfo = jwt.getClaim(JwtConstant.SAVE_OBJ_KEY).asString();// 存入user
        return new JsonFactory().createParser(tokenInfo);
    }


    /**
     * 验证并获取用户存储信息
     *
     * @param token token
     * @return UserTokenUtil
     */
    public static UserTokenUtil getTokenInfoByToken(String token) throws IOException {
        Algorithm algorithm = Algorithm.HMAC256(JwtConstant.SECRET_KEY);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(JwtConstant.ISSUER) // 用于说明该JWT是由谁签发的
                .withSubject(JwtConstant.SUBJECT_OBJ) // 用于说明该JWT面向的对象
                .withAudience(JwtConstant.SAVE_OBJ_KEY) // 用于说明该JWT发送给的用户
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String tokenInfo = jwt.getClaim(JwtConstant.SAVE_OBJ_KEY).asString();// 存入user
        return objectMapper.readValue(new JsonFactory().createParser(tokenInfo), UserTokenUtil.class);
    }

}
