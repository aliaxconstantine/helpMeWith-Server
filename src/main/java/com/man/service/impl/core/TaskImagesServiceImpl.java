package com.man.service.impl.core;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.dto.HttpResult;
import com.man.entity.core.TaskImages;
import com.man.service.CoreService.TaskImagesService;
import com.man.mapper.TaskImagesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 艾莉希雅
* &#064;description  针对表【task_images】的数据库操作Service实现
* &#064;createDate  2024-03-10 21:42:22
 */
@Service
public class TaskImagesServiceImpl extends ServiceImpl<TaskImagesMapper, TaskImages>
    implements TaskImagesService{

    @Transactional
    @Override
    public HttpResult submitTaskProgress(List<String> taskImagesList, Long taskId) {
        taskImagesList.forEach(item ->{
            TaskImages images = TaskImages.builder().taskId(taskId).imageUrl(item).build();
            save(images);
        });
        return HttpResult.success(true);
    }

    @Override
    public HttpResult getTaskProgresses(Long taskId) {
        List<TaskImages> list = query().eq("task_id", taskId).list();
        return HttpResult.success(list);
    }
}




