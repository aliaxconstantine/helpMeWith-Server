package com.man.service.impl.core;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.dto.TaskForm;
import com.man.entity.core.Task;
import com.man.entity.core.TaskTimes;
import com.man.service.CoreService.TaskTimesService;
import com.man.mapper.TaskTimesMapper;
import org.springframework.stereotype.Service;

/**
* @author 艾莉希雅
* @description 针对表【task_times】的数据库操作Service实现
* @createDate 2023-11-28 22:16:07
*/
@Service
public class TaskTimesServiceImpl extends ServiceImpl<TaskTimesMapper, TaskTimes>
    implements TaskTimesService{

    @Override
    public void setDefault(TaskForm task) {

    }
}




