package com.HelpMe.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.HelpMe.Entity.core.TaskCategory;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 艾莉希雅
* @description 针对表【task_categoriy】的数据库操作Mapper
* @createDate 2023-11-20 16:56:24
* @Entity com.man.entity.core.TaskCategoriy
*/
@Mapper
public interface TaskCategoriyMapper extends BaseMapper<TaskCategory> {

}




