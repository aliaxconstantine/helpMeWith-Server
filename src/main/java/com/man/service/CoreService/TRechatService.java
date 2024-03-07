package com.man.service.CoreService;

import com.man.dto.HttpResult;
import com.man.dto.RecodeFrom;
import com.man.entity.core.TRechat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【t_rechat】的数据库操作Service
* @createDate 2023-11-25 17:09:04
*/
public interface TRechatService extends IService<TRechat> {
    HttpResult updateList(List<RecodeFrom> recodeFromList);
    HttpResult getAll();
}
