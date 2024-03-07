package com.man.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.man.entity.core.Communication;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【communications】的数据库操作Mapper
* @createDate 2023-08-20 10:32:30
* @Entity com.bean.Communications
*/
@Mapper
public interface CommunicationsMapper extends BaseMapper<Communication> {
    List<Communication> getCommunicationListById(@Param("userId") Long userId, @Param("otherId")Long otherId);
}




