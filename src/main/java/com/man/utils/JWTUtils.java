package com.man.utils;


import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Lang;
import org.springframework.stereotype.Component;

import java.util.*;

import com.auth0.jwt.*;

@Component
@Log4j2
/**
 * JWTUtils是一个工具类，用于创建和验证JSON Web Token（JWT）。
 * JWT是一种用于在网络应用间传递信息的安全方法，它由三部分组成：头部（header）、
 * 负载（payload）和签名（signature）。
 *
 * @note 该工具类提供了创建JWT、验证JWT以及从JWT中获取用户信息和用户权限的功能。
 */
public class JWTUtils {

    private static final String secret = "secret888";

    /**
     * 创建JWT token。
     *
     * @param userInfo 用户信息。
     * @param authList 用户权限列表。
     * @return 返回生成的JWT token。
     *
     * @note 该方法使用指定的用户信息和权限列表创建JWT token，并设置签发人、签发时间、过期时间等信息。
     * 密钥是通过HMAC256算法基于指定的密钥字符串生成。
     */
    public static String createJWT(String userName, Long userId, String userInfo, List<String> authList) {
        Date issDate = new Date();
        Date expireDate = new Date(issDate.getTime() + 1000 * 60 * 60 * 2);
        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");
        return JWT.create().withHeader(headerClaims)
                .withIssuer("aliax")//签发人
                .withIssuedAt(issDate)
                .withExpiresAt(expireDate)
                .withClaim("userName", userName)
                .withClaim("userId",userId)
                .withClaim("userInfo", userInfo)
                .withClaim("userAuth", authList)
                .sign(Algorithm.HMAC256(secret));//密钥
    }

    /**
     * 验证JWT token的有效性。
     *
     * @param jwt JWT token。
     * @return 如果JWT token有效，则返回true；否则返回false。
     *
     * @note 该方法使用指定的密钥验证JWT token的有效性，如果验证通过则返回true；否则返回false。
     */
    public static boolean verifyToken(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT verify = jwtVerifier.verify(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从JWT token中获取用户信息。
     *
     * @param jwt JWT token。
     * @return 返回从JWT token中获取的用户信息，如果解析失败则返回null。
     *
     * @note 该方法从指定的JWT token中解析出用户信息，并以整数形式返回。
     * 如果解析失败，则返回null。
     */
    public static Integer getUserInfoFromToken(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT verify = jwtVerifier.verify(jwt);
            return verify.getClaim("userID").asInt();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从JWT token中获取用户信息。
     *
     * @param jwt JWT token。
     * @return 返回从JWT token中获取的用户名，如果解析失败则返回null。
     *
     * @note 该方法从指定的JWT token中解析出用户名，并以字符串形式返回。
     * 如果解析失败，则返回null。
     */
    public static String getUsernameFromToken(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT verify = jwtVerifier.verify(jwt);
            return verify.getClaim("userName").asString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从JWT token中获取用户权限列表。
     *
     * @param jwt JWT token。
     * @return 返回从JWT token中获取的用户权限列表，如果解析失败则返回null。
     *
     * @note 该方法从指定的JWT token中解析出用户权限列表，并以字符串列表形式返回。
     * 如果解析失败，则返回null。
     */
    public static List<String> getUserAuth(String jwt) {
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT verify = jwtVerifier.verify(jwt);
            return verify.getClaim("userAuth").asList(String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getId(String jwt){
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT verify = jwtVerifier.verify(jwt);
            return verify.getClaim("userId").asLong();
        } catch (Exception e) {
            return null;
        }
    }

}
