package com.HelpMe.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.HelpMe.Entity.core.TUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;
@Mapper
public interface UserMapper extends BaseMapper<TUser> {
    @MapKey("id")
    Map<Long, TUser> SelectAllUser();
    TUser SelectUserToId(Long id);
    TUser SelectSimpleUserById(int id);
    TUser selectUserByPhone(String phone);
    TUser selectUserByLoginName(String loginName);
}
