package com.man.config.secuityconfig.secuityEvent;

import com.man.dto.ErrorCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.man.dto.HttpResult;
import com.man.dto.SecurityUser;
import com.man.entity.core.TUser;
import com.man.service.CoreService.TUserService;
import com.man.service.impl.Security.UserServiceImpl;
import com.man.utils.JWTUtils;
import com.man.utils.JsonUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于在认证成功后处理生成JWT Token等相关逻辑的处理器。
 */
@Component
@Log4j2
public class CAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 重写onAuthenticationSuccess方法，在认证成功后生成JWT Token并返回给客户端。
     * @param request HttpServletRequest对象，表示当前请求。
     * @param response HttpServletResponse对象，表示当前响应。
     * @param authentication Authentication对象，表示当前认证信息。
     * @throws IOException 当操作IO流时可能会抛出IOException异常。
     * @throws ServletException 当Servlet出现异常时可能会抛出ServletException异常。
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("认证成功");
        // 获取当前认证成功的用户信息
        SecurityUser principal = (SecurityUser) authentication.getDetails();
        // 将用户信息转换成JSON字符串
        String strUserInfo = objectMapper.writeValueAsString(principal);
        // 获取当前用户具有的权限列表
        List<SimpleGrantedAuthority> authorities = (List<SimpleGrantedAuthority>) principal.getAuthorities();
        List<String> auth = authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList());
        // 创建自定义的Authentication对象
        Authentication customAuthentication = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), authorities);
        //设置认证上下文
        SecurityContextHolder.getContext().setAuthentication((customAuthentication));
        // 根据用户信息和权限列表生成JWT Token
        String jwt = JWTUtils.createJWT(principal.getUsername(),principal.getUserId(),strUserInfo, auth);
        // 构建响应内容
        HttpResult httpResult = HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).msg("Token生成成功").data(jwt).build();
        // 输出响应内容
        JsonUtil.printToJson(httpResult,response);
    }

}

