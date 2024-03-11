package com.man.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.man.entity.core.TOrder;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 艾莉希雅
* @description 针对表【orders】的数据库操作Mapper
* @createDate 2023-10-14 17:53:56
* @Entity com.entity.core.Orders
*/
@Mapper
public interface OrdersMapper extends BaseMapper<TOrder> {

}




