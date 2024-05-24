package com.man.controller.config.secuityconfig.secuityfliter;
import com.man.utils.AuthenticationUtils;
import com.man.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//用于手机号登录
public class UserLoginWithPhoneFilter extends UsernamePasswordAuthenticationFilter {
    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    public UserLoginWithPhoneFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //判断是否为Post请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        try {
            response = ResponseUtils.CORS(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.getAuthenticationManager().authenticate(AuthenticationUtils.getAuthentication(request,
                stringRedisTemplate, getUsernameParameter(), getPasswordParameter()));
    }

}
