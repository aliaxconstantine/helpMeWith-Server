package com.man.service.CoreService;

import com.man.dto.HttpResult;
import com.man.entity.core.UserLikeMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 艾莉希雅
* @description 针对表【user_like_message】的数据库操作Service
* @createDate 2024-03-03 16:38:29
*/
public interface UserLikeMessageService extends IService<UserLikeMessage> {

    HttpResult addLike(String messageId);

    HttpResult delLike(String messageId);

    HttpResult getOk(String messageId);
}
