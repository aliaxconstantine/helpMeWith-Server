package com.HelpMe.Service.CoreService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.HelpMe.Entity.Security.SysUser;
import com.HelpMe.Entity.core.TUser;

/**
* @author 艾莉希雅
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2023-09-11 10:38:43
*/
public interface SysUserService extends IService<SysUser> {
    SysUser getByUserName(String userName);

    SysUser getByUserId(Long userId);

    SysUser saveSysUser(TUser tUser);
}
