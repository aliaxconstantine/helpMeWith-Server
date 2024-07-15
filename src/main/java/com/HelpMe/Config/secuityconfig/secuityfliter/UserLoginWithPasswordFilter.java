package com.HelpMe.Config.secuityconfig.secuityfliter;
import com.HelpMe.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import static com.HelpMe.utils.AuthenticationUtils.checkVerifyCode;
import static com.HelpMe.utils.RedisConstants.*;

@Log4j2
public class UserLoginWithPasswordFilter extends UsernamePasswordAuthenticationFilter {
    private final StringRedisTemplate stringRedisTemplate;

    public UserLoginWithPasswordFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 尝试进行身份验证的方法，重写自父类。第一层
     * @param request HttpServletRequest对象，表示当前请求。
     * @param response HttpServletResponse对象，表示当前响应。
     * @return 返回一个身份验证的 Authentication 对象。
     * @throws AuthenticationException 当身份验证失败时可能会抛出 AuthenticationException 异常。
     */
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ResponseUtils.CORS(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 需要是 POST 请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        String username = request.getParameter(getUsernameParameter());
        String password = request.getParameter(getPasswordParameter());
        log.info("密码登录 账号为"+ username+"密码为"+password);
        stringRedisTemplate.afterPropertiesSet();
        String cacheCode =  stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY + username);
        //校验验证码
        String code = request.getParameter("code");
        checkVerifyCode(cacheCode, code);
        // 校验用户名和密码是否为空
        if(ObjectUtils.isEmpty(username)){
            throw new AuthenticationServiceException("用户名不能为空");
        }
        if(ObjectUtils.isEmpty(password)){
            throw new AuthenticationServiceException("密码不能为空");
        }
        // 构建身份验证请求对象
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        try {
            response = ResponseUtils.CORS(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 使用身份验证管理器进行身份验证
        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
