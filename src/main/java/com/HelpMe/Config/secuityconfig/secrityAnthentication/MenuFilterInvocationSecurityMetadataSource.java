package com.HelpMe.Config.secuityconfig.secrityAnthentication;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Log4j2
public class MenuFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Set<ConfigAttribute> attributes = new HashSet<>();
        // 获取请求URL
        String requestUrl = ((FilterInvocation) object).getRequestUrl();
        log.info("requestUrl >> {}", requestUrl);

        // 获取当前用户的权限信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetails)authentication.getDetails();
        var userAuthorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // 比较URL后缀与权限后缀
        for (String authority : userAuthorities) {
            if (requestUrl.endsWith(authority)) {
                attributes.add(new SecurityConfig(authority));
            }
        }

        // 如果没有匹配的权限后缀，默认为需要登录权限
        if (attributes.isEmpty()) {
            attributes.add(new SecurityConfig("ROLE_LOGIN"));
        }
        return attributes;
    }
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
