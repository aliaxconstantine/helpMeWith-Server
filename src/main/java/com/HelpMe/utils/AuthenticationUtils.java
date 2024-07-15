package com.HelpMe.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;

/**
 * @author 艾莉希雅
 */
public class AuthenticationUtils {
    public static void checkVerifyCode(String redisVerifyCode, String code) {
        if (ObjectUtils.isEmpty(code)){
            throw new AuthenticationServiceException("验证码不能为空!");
        }
        if(ObjectUtils.isEmpty(redisVerifyCode)){
            throw new AuthenticationServiceException("请重新申请验证码!");
        }
        if (!redisVerifyCode.equalsIgnoreCase(code)) {
            throw new AuthenticationServiceException("验证码错误!");
        }
    }

    public static Authentication getAuthentication(HttpServletRequest request, StringRedisTemplate stringRedisTemplate, String usernameParameter, String passwordParameter) {
        String phone = request.getParameter(usernameParameter);
        String code = request.getParameter(passwordParameter);

        String cacheCode = null;
        try{
            cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY+phone);
        }catch (Exception e)
        {
            throw new AuthenticationServiceException("服务器内部错误");
        }
        checkVerifyCode(cacheCode,code);
        return new UsernamePasswordAuthenticationToken(phone,code);
    }

    public static Long getId(){
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
    }

    public static String getName(){
        return  SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }

}
