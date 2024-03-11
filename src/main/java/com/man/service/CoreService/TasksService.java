package com.man.service.CoreService;


import com.baomidou.mybatisplus.extension.service.IService;
import com.man.dto.HttpResult;
import com.man.dto.TaskForm;
import com.man.entity.core.Task;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 艾莉希雅
* @description 针对表【tasks】的数据库操作Service
* @createDate 2023-08-20 10:37:17
*/
public interface TasksService extends IService<Task> {


    HttpResult queryTaskById(Long id);
    HttpResult update(Task task);

    @Transactional
    boolean updateGEO(Task task);

    //用户承接任务
    @Transactional
    HttpResult acceptTask(Long taskId);
    @Transactional
    HttpResult addMessageCount(Long taskId);

    //获取状态不同，类型不同的task列表
    HttpResult getTasksBySortKey(Integer pageNum, Integer type, Double x, Double y);

    HttpResult getTasksByTime(Integer pageNum);


    @Transactional
    HttpResult confirmTaskCompletion(Long taskId);

    HttpResult successTask(String taskId);

    HttpResult getTasksByLocation(Integer PageNum, Double x, Double y);

    //创建订单
    @Transactional
    HttpResult createTask(TaskForm task);

    HttpResult getOtherTasksBySortKey(Long otherUserId, Integer sortKey,Integer pageNum);

    HttpResult getTaskTime(Long taskId);

    HttpResult getTypeTasks(String type, Integer pageNum);

    HttpResult getByKey(String enkey,Integer pageNum);
}
