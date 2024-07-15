package com.HelpMe.mapper;

import com.HelpMe.Entity.core.Friend;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【friend】的数据库操作Mapper
* @createDate 2023-10-13 15:24:54
* @Entity com.entity.core.Friend
*/
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
    List<Friend> getByFriendIdFriendList(@Param("userId") Long userId);
}




