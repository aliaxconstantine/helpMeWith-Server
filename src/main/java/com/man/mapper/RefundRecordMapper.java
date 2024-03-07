package com.man.mapper;

import com.man.entity.core.RefundRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 艾莉希雅
* @description 针对表【refund_record】的数据库操作Mapper
* @createDate 2023-10-18 13:30:21
* @Entity com.entity.core.RefundRecord
*/
@Mapper
public interface RefundRecordMapper extends BaseMapper<RefundRecord> {

}




