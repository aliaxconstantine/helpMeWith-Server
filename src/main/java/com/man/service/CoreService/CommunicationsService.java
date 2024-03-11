package com.man.service.CoreService;

import com.baomidou.mybatisplus.extension.service.IService;
import com.man.dto.HttpResult;
import com.man.entity.core.Communication;
import org.springframework.transaction.annotation.Transactional;

/**
* @author 艾莉希雅
* @description 针对表【communications】的数据库操作Service
* @createDate 2023-08-20 10:32:30
*/
public interface CommunicationsService extends IService<Communication> {

    @Transactional
    HttpResult sendChatMessage(String message, String otherUserId, Long meId);

    HttpResult getChatMessagesByUserId(String userId);

    HttpResult deleteChatMessageById(String chatId);

    @Transactional
    HttpResult ReMessage(String reMessageId, String message);
}
