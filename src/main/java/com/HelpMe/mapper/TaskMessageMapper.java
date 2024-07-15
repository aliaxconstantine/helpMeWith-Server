package com.HelpMe.mapper;

import com.HelpMe.Entity.core.TaskMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 艾莉希雅
* @description 针对表【task_message】的数据库操作Mapper
* @createDate 2023-10-07 18:43:10
* @Entity com.POJO.Security.TaskMessage
*/
@Mapper
public interface TaskMessageMapper extends BaseMapper<TaskMessage> {

}




