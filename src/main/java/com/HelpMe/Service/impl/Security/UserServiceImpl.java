package com.HelpMe.Service.impl.Security;

import com.HelpMe.Entity.Security.SysUser;
import com.HelpMe.Entity.core.TUser;
import com.HelpMe.mapper.SysUserMapper;
import com.HelpMe.dto.SecurityUser;
import com.HelpMe.mapper.UserMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final SysMenuServiceImpl sysMenuService;
    private final UserMapper userMapper;
    public UserServiceImpl(SysUserMapper sysUserMapper, SysMenuServiceImpl sysMenuService, UserMapper userMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysMenuService = sysMenuService;
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.getSystemUserByName(username);
        TUser tUser = userMapper.selectUserByLoginName(username);
        System.out.println(username);
        if(user == null){
            throw new UsernameNotFoundException("用户不存在");
        }
        SecurityUser securityUser = new SecurityUser(user, tUser.getId());
        List<String> userPermissions = sysMenuService.queryPermissionByUserId(user.getId());
        List<SimpleGrantedAuthority> authorities1List = userPermissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        securityUser.setAuthorities(authorities1List);
        return securityUser;
    }

}
