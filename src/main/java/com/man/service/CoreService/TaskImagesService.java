package com.man.service.CoreService;

import com.man.dto.HttpResult;
import com.man.entity.core.TaskImages;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Lang;

import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【task_images】的数据库操作Service
* @createDate 2024-03-10 21:42:22
*/
public interface TaskImagesService extends IService<TaskImages> {


    HttpResult getTaskProgresses(Long taskId);

    HttpResult submitTaskProgress(List<String> imagesList, Long taskId);
}
