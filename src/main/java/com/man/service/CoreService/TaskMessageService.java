package com.man.service.CoreService;

import com.man.dto.HttpResult;
import com.man.entity.core.TaskMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 艾莉希雅
* @description 针对表【task_message】的数据库操作Service
* @createDate 2023-10-07 18:43:10
*/
public interface TaskMessageService extends IService<TaskMessage> {
    HttpResult getTaskChat(String taskId);
    HttpResult updateState(long conversationId, boolean b);

    HttpResult getLike(String messageId);
}
