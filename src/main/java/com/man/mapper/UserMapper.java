package com.man.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.man.entity.core.TUser;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

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
