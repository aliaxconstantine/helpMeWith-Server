package com.man.service.CoreService;

import com.man.entity.core.SysMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 艾莉希雅
* @description 针对表【sys_message】的数据库操作Service
* @createDate 2024-03-04 16:45:50
*/
public interface SysMessageService extends IService<SysMessage> {

    void sendMessage(String userId, String message);
}
