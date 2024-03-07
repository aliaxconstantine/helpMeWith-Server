package com.man.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.man.entity.core.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【tasks】的数据库操作Mapper
* @createDate 2023-08-20 10:37:17
* @Entity com.bean.Tasks
*/
@Mapper
public interface TasksMapper extends BaseMapper<Task> {
    List<Task> getAllByStatusTasks(Long userId,String status);
    List<Task> getAllByTypeTasks(String type,Integer pageNum);
    List<Task> getByKeyTasks(String key,Integer pageNum);
}




