package com.man.service.impl.Security;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.entity.Security.SysMenu;
import com.man.entity.Security.SysUser;
import com.man.entity.core.TUser;
import com.man.mapper.SysMenuMapper;
import com.man.service.CoreService.SysUserService;
import com.man.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
* @author 艾莉希雅
* @description 针对表【sys_user】的数据库操作Service实现
* @createDate 2023-09-11 10:38:43
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper sysUserMapper;

    @Autowired
    public SysUserServiceImpl(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;

    }
    @Override
    public SysUser getByUserName(String userName) {
        return sysUserMapper.getSystemUserByName(userName);
    }

    @Override
    public SysUser getByUserId(Long userId) {
        return null;
    }

    @Override
    public SysUser saveSysUser(TUser tUser) {
        return null;
    }


}




