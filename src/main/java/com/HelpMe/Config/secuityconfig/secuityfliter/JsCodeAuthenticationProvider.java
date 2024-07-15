package com.HelpMe.Config.secuityconfig.secuityfliter;

import com.HelpMe.Service.impl.Security.UserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * JsCodeAuthenticationProvider（JsCode认证提供者）是一个类，实现了AuthenticationProvider接口。
 * 该类的作用是处理JsCode认证，并进行相应的身份验证。
 *
 * 在Spring Security中，AuthenticationProvider接口用于验证用户的身份信息。JsCodeAuthenticationProvider作为
 * 自定义的认证提供者，通过重写authenticate()方法来实现JsCode的身份验证逻辑。
 *
 * 该类通过@Resource注解注入了SysUserMapper对象，可以使用sysUserMapper来访问用户相关的数据和操作。
 *
 * @note 在authenticate()方法中，你可以根据JsCode进行身份验证的具体逻辑，例如根据JsCode从数据库查询用户信息、
 * 验证JsCode的有效性等。在验证成功后，可以将验证结果封装为一个Authentication对象返回。
 * 你可以根据具体的业务需求，在该类中添加其他方法或属性来支持JsCode认证提供者的自定义行为。
 */
@Component
@Log4j2
public class JsCodeAuthenticationProvider implements AuthenticationProvider {

    private final UserServiceImpl sysUserService;
    @Autowired
    public JsCodeAuthenticationProvider(UserServiceImpl sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 该方法用于进行JsCode认证，并进行相应的身份验证。
     *
     * 在Spring Security中，AuthenticationProvider接口用于验证用户的身份信息。
     * 该方法通过重写authenticate()方法来实现JsCode的身份验证逻辑。
     *
     * @param authentication Authentication对象，表示待验证的身份信息。
     * @return 返回一个Authentication对象，表示经过身份验证后的结果。
     * @throws AuthenticationException 身份验证异常，当身份验证失败时抛出。
     *
     * @note 在该方法中，你可以根据JsCode进行身份验证的具体逻辑，例如根据JsCode从数据库查询用户信息、验证JsCode的有效性等。
     * 在验证成功后，可以将验证结果封装为一个Authentication对象返回。
     * 你可以根据具体的业务需求，在该类中添加其他方法或属性来支持JsCode认证提供者的自定义行为。
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("正在校验");
        authentication.getDetails();
        UsernamePasswordAuthenticationToken unt = (UsernamePasswordAuthenticationToken)authentication;
        String name = authentication.getPrincipal().toString();
        var principal = sysUserService.loadUserByUsername(name);
        unt.setDetails(principal);
        return authentication;
    }


    /**
     * 该方法用于判断该认证提供者是否支持指定类型的认证。
     *
     * @param authentication 认证类型。
     * @return 如果该认证提供者支持指定类型的认证，则返回true；否则返回false。
     *
     * @note 在supports()方法中，你可以根据需要判断该认证提供者是否支持指定类型的认证。
     * 该方法可以用于配置Spring Security中的AuthenticationManager，以确定使用哪个认证提供者来处理特定类型的认证。
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}


