package com.man.dto;

import com.man.entity.Security.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUser implements UserDetails {
    private List<SimpleGrantedAuthority> authorities;
    private final SysUser sysUser;
    public SysUser getSysUser(){
        return sysUser;
    }

    public SecurityUser(SysUser sysUser, Long userId){
        this.sysUser=sysUser;
        this.userId = userId;
    }
    private final Long userId;
    public Long getUserId(){return userId;}
    //返回权限
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    public void setAuthorities(List<SimpleGrantedAuthority> authorities){
        this.authorities = authorities;
    }
    @Override
    public String getPassword() {
        String passWord = sysUser.getPassword();
        sysUser.setPassword(null); //防止密码发送给前端
        return passWord;
    }
    @Override
    public String getUsername() { return this.sysUser.getUsername();}
    @Override
    public boolean isAccountNonExpired() { return sysUser.getAccountNoExpired().equals(1);}
    @Override
    public boolean isAccountNonLocked() {
        return sysUser.getAccountNoLocked().equals(1);
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return sysUser.getCredentialsNoExpired().equals(1);
    }
    @Override
    public boolean isEnabled() {return sysUser.getEnabled().equals(1);}
}
