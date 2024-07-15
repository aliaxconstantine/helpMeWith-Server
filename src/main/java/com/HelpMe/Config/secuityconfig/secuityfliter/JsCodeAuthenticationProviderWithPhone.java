package com.HelpMe.Config.secuityconfig.secuityfliter;
import com.HelpMe.Service.CoreService.SysUserService;
import com.HelpMe.dto.LoginForm;
import com.HelpMe.dto.SecurityUser;
import com.HelpMe.Entity.Security.SysUser;
import com.HelpMe.Entity.core.TUser;
import com.HelpMe.Service.CoreService.TUserService;
import com.HelpMe.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JsCodeAuthenticationProviderWithPhone implements AuthenticationProvider {

    public final TUserService tUserService;
    public final SysUserService sysUserService;

    @Autowired
    public JsCodeAuthenticationProviderWithPhone(TUserService tUserService, SysUserService sysUserService) {
        this.tUserService = tUserService;
        this.sysUserService = sysUserService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken phoneCodeAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        String phone = (String) phoneCodeAuthenticationToken.getPrincipal();
        String code = (String) phoneCodeAuthenticationToken.getCredentials();
        if(!RegexUtils.isPhoneInvalid(phone)){
            throw new AuthenticationServiceException("手机号错误");
        }
        //调用login方法
        boolean isTrue = tUserService.Login(LoginForm.builder().phone(phone).code(code).build());
        if(!isTrue){
            throw new AuthenticationServiceException("验证码错误");
        }
        //数据库获取user名称，调用名称获取系统权限角色
        TUser user = tUserService.getByPhone(phone);
        SysUser sysUser = sysUserService.getById(user.getId());
        if(sysUser == null){
            throw new AuthenticationServiceException("找不到该用户");
        }
        SecurityUser securityUser = new SecurityUser(sysUser, user.getId());

        return new UsernamePasswordAuthenticationToken(sysUser.getUsername(), null, securityUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
