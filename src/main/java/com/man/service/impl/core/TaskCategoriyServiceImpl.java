package com.man.service.impl.core;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.dto.HttpResult;
import com.man.dto.TaskForm;
import com.man.entity.core.TaskCategory;
import com.man.service.CoreService.TaskCategoriyService;
import com.man.mapper.TaskCategoriyMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【task_categoriy】的数据库操作Service实现
* @createDate 2023-11-20 16:56:24
*/
@Service
public class TaskCategoriyServiceImpl extends ServiceImpl<TaskCategoriyMapper, TaskCategory> implements TaskCategoriyService{

    private final TaskCategoriyMapper taskCategoriyMapper;

    public TaskCategoriyServiceImpl(TaskCategoriyMapper taskCategoriyMapper) {
        this.taskCategoriyMapper = taskCategoriyMapper;
    }

    @Override
    public HttpResult getAllCate() {
        QueryWrapper<TaskCategory> taskCategoryQueryWrapper = new QueryWrapper<>();
        taskCategoryQueryWrapper.orderByAsc("hot_num");
        taskCategoryQueryWrapper.last("LIMIT 30");
        var rest = taskCategoriyMapper.selectList(taskCategoryQueryWrapper);
        if(rest == null) {
            return HttpResult.fail("请稍后再试");
        }
        return HttpResult.success(rest);
    }

    @Override
    public void submitNew(TaskForm taskForm){
        List<String> typelist = Arrays.stream(taskForm.getType()).toList();
        typelist.forEach( t -> {
                    QueryWrapper<TaskCategory> wrapper = new QueryWrapper<>();
                    wrapper.eq("category", t);
                    TaskCategory taskCategory = taskCategoriyMapper.selectOne(wrapper);
                    if(taskCategory == null){
                        var type = TaskCategory.builder()
                                .category(t)
                                .bigClass(taskForm.getBigType())
                                .hotNum(1)
                                .build();
                        taskCategoriyMapper.insert(type);
                    }});
    }

}




