package com.HelpMe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.HelpMe.Entity.Security.SysMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【sys_menu】的数据库操作Mapper
* @createDate 2023-09-11 16:17:24
* @Entity com.POJO.Security.SysMenu
*/
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<String> permissionByUserId(@Param("userId") Long userId);
    void addRuleUser(Long userId,Long roleId);
}




