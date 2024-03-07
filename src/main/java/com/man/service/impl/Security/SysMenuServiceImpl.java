package com.man.service.impl.Security;


import com.man.mapper.SysMenuMapper;
import com.man.service.CoreService.SysMenuService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【sys_menu】的数据库操作Service实现
* @createDate 2023-09-11 16:17:24
*/
@Service
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    SysMenuMapper sysMenuMapper;
    public List<String> queryPermissionByUserId(Long userId) {
       return sysMenuMapper.permissionByUserId(userId);
    }
}




