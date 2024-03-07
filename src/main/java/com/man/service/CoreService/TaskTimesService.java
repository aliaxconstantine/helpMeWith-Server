package com.man.service.CoreService;

import com.man.dto.TaskForm;
import com.man.entity.core.TaskTimes;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 艾莉希雅
* @description 针对表【task_times】的数据库操作Service
* @createDate 2023-11-28 22:16:07
*/
public interface TaskTimesService extends IService<TaskTimes> {

    void setDefault(TaskForm task);
}
