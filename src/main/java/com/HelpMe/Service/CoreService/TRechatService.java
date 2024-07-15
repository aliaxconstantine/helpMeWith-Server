package com.HelpMe.Service.CoreService;

import com.HelpMe.dto.HttpResult;
import com.HelpMe.dto.RecodeFrom;
import com.HelpMe.Entity.core.TRechat;
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
