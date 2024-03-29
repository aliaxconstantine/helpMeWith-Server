package com.man.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.man.entity.core.Advertisement;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 艾莉希雅
* @description 针对表【advertisements】的数据库操作Mapper
* @createDate 2023-08-20 10:30:21
* @Entity com.bean.Advertisements
*/
@Mapper
public interface AdvertisementsMapper extends BaseMapper<Advertisement> {

}




