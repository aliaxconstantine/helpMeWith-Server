package com.HelpMe.Service.CoreService;

import com.HelpMe.dto.HttpResult;

import com.baomidou.mybatisplus.extension.service.IService;
import com.HelpMe.dto.TaskForm;
import com.HelpMe.Entity.core.TaskCategory;

/**
* @author 艾莉希雅
* @description 针对表【task_categoriy】的数据库操作Service
* @createDate 2023-11-20 16:56:24
*/
public interface TaskCategoriyService extends IService<TaskCategory> {
    HttpResult getAllCate();

    void submitNew(TaskForm taskForm);
}
