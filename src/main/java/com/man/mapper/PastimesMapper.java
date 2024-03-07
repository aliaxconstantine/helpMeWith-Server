package com.man.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.man.entity.core.TaskTimes;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
* @author 艾莉希雅
* @description 针对表【tasktimes】的数据库操作Mapper
* @createDate 2023-09-10 13:19:51
* @Entity com.bean.Tasktimes
*/
@Mapper
public interface PastimesMapper extends BaseMapper<TaskTimes> {

}




