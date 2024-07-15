package com.HelpMe.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.HelpMe.Entity.Security.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 艾莉希雅
* @description 针对表【sys_user】的数据库操作Mapper
* @createDate 2023-09-11 10:38:43
* @Entity com.bean.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser getSystemUserByName(@Param("userName") String userName);
}




