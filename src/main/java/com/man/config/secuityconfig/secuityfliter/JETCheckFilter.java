package com.man.config.secuityconfig.secuityfliter;

import com.man.dto.HttpResult;
import com.man.utils.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 用于进行JWT Token验证的过滤器。
 */
@Log4j2
public class JETCheckFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 在过滤器中实现JWT Token验证的主要逻辑。
     * @param request HttpServletRequest对象，表示当前请求。
     * @param response HttpServletResponse对象，表示当前响应。
     * @param filterChain FilterChain对象，表示当前过滤器链。
     * @throws ServletException 当Servlet出现异常时可能会抛出ServletException异常。
     * @throws IOException 当操作IO流时可能会抛出IOException异常。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        try {
            ResponseUtils.CORS(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 获取请求的URL路径
        request.setCharacterEncoding("UTF-8");
        String requestURL = request.getRequestURI();
        // 如果请求的URL为"/loginBack"，则不进行鉴权处理，直接跳过
        // "/user/code","/user/phoneLogin","/userLogin","/swagger-ui.html"
        if(Arrays.asList(SystemConstants.publicUrls).contains(request.getRequestURL().toString())){
            doFilter(request,response,filterChain);
            return;
        }
        // 获取请求头中的Authorization字段
        String strAuth = request.getHeader("Authorization");
        // 如果Authorization字段为空，则返回错误信息
        if(ObjectUtils.isEmpty(strAuth)){
            JsonUtil.printToJson(HttpResult.fail("认证信息为空"),response);
            return;
        }
        // 从Authorization字段中解析出Token
        String jwtToken = strAuth.replace("bearer","");
        // 如果Token为空，则返回错误信息
        if(ObjectUtils.isEmpty(jwtToken)){
            JsonUtil.printToJson(HttpResult.fail("认证信息缺失"),response);
            log.error("登录请求信息 token为空");
            return;
        }
        // 进行JWT Token验证，如果验证失败，则返回错误信息
        if(!JWTUtils.verifyToken(jwtToken)){
            JsonUtil.printToJson(HttpResult.fail("登录失败，请重新登录"),response);
            return;
        }
        //jwt权限列表授权
        var auths = JWTUtils.getUserAuth(jwtToken);
        var name = JWTUtils.getUsernameFromToken(jwtToken);
        var userId = JWTUtils.getId(jwtToken);
        if(name == null){
            JsonUtil.printToJson(HttpResult.fail("用户信息错误"),response);
            return;
        }
        //从token获取信息存储在安全上下文
        Authentication customAuthentication = null;
        if (auths != null) {
            customAuthentication = new UsernamePasswordAuthenticationToken(name, userId, convert(auths));
        }
        SecurityContextHolder.getContext().setAuthentication(customAuthentication);
        // 验证通过，放行请求
        filterChain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return 1; // 设置合适的顺序值
    }
    public static Collection<? extends GrantedAuthority> convert(List<String> stringList) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        for (String authority : stringList) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority);
            authorityList.add(grantedAuthority);
        }
        return authorityList;
    }
}
