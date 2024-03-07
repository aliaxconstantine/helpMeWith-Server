package com.man.service.CoreService;

import com.man.dto.HttpResult;

import com.baomidou.mybatisplus.extension.service.IService;
import com.man.dto.TaskForm;
import com.man.entity.core.TaskCategory;

/**
* @author 艾莉希雅
* @description 针对表【task_categoriy】的数据库操作Service
* @createDate 2023-11-20 16:56:24
*/
public interface TaskCategoriyService extends IService<TaskCategory> {
    HttpResult getAllCate();

    void submitNew(TaskForm taskForm);
}
