package com.HelpMe.Service.impl.core;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.HelpMe.dto.ErrorCodeEnum;
import com.HelpMe.dto.HttpResult;
import com.HelpMe.Entity.core.Communication;
import com.HelpMe.mapper.CommunicationsMapper;
import com.HelpMe.mapper.TRechatMapper;
import com.HelpMe.Service.CoreService.CommunicationsService;
import com.HelpMe.utils.AuthenticationUtils;
import com.HelpMe.utils.CacheClient;
import com.HelpMe.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

/**
* @author 艾莉希雅
* @description 针对表【communications】的数据库操作Service实现
* @createDate 2023-08-20 10:32:30
*/
@Service
public class CommunicationsServiceImpl extends ServiceImpl<CommunicationsMapper, Communication> implements CommunicationsService {

    private final CacheClient cacheClient;
    public final CommunicationsMapper communicationsMapper;
    public final TRechatMapper rechatMapper;
    public final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public CommunicationsServiceImpl(CacheClient cacheClient, CommunicationsMapper communicationsMapper, TRechatMapper rechatMapper, StringRedisTemplate stringRedisTemplate) {
        this.cacheClient = cacheClient;
        this.communicationsMapper = communicationsMapper;
        this.rechatMapper = rechatMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Transactional
    @Override
    public HttpResult sendChatMessage(String message, String otherUserId,Long meId ) {
        //给好友发消息
        long id = Long.parseLong(otherUserId);
        Communication taskMessage = Communication.builder()
                .senderUserId(meId)
                .receiverUserId(id)
                .content(message)
                .date(new Timestamp(System.currentTimeMillis()))
                .build();
        //先缓存
        String key = RedisConstants.CHATMESSAGE_KEY+ System.currentTimeMillis();
        stringRedisTemplate.opsForHash().put(key,AuthenticationUtils.getId(),taskMessage);
        return HttpResult.builder().msg("发送成功").code(ErrorCodeEnum.SUCCESS.code).data(true).build();
    }

    //获取所有消息
    @Override
    public HttpResult getChatMessagesByUserId(String otherIdStr) {
        Long userId = AuthenticationUtils.getId();
        Long otherId = Long.parseLong(otherIdStr);
        var communicationList =  communicationsMapper.getCommunicationListById(userId,otherId);
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).data(communicationList).msg("获取成功").build();
    }

    //删除
    @Transactional
    @Override
    public HttpResult deleteChatMessageById(String messageId) {
        boolean ifTrue = removeById(messageId);
        if(!ifTrue){
            return HttpResult.builder().code(ErrorCodeEnum.FAIL.code).msg("删除失败").build();
        }
        return HttpResult.builder().code(ErrorCodeEnum.SUCCESS.code).msg("获取成功").build();
    }

    //回复消息
    @Transactional
    @Override
    public HttpResult ReMessage(String reMessageId, String message){
        return null;
    }
}




