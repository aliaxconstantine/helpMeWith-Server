package com.HelpMe.Service.impl.core;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.HelpMe.dto.HttpResult;
import com.HelpMe.dto.TaskMessageEnum;
import com.HelpMe.Entity.core.TaskMessage;
import com.HelpMe.mapper.UserMapper;
import com.HelpMe.Service.CoreService.TaskMessageService;
import com.HelpMe.mapper.TaskMessageMapper;
import com.HelpMe.utils.CacheClient;
import com.HelpMe.utils.RedisConstants;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
* @author 艾莉希雅
* @description 针对表【task_message】的数据库操作Service实现
* @createDate 2023-10-07 18:43:10
*/
@Service
public class TaskMessageServiceImpl extends ServiceImpl<TaskMessageMapper, TaskMessage> implements TaskMessageService{

    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;

    @Autowired
    public TaskMessageServiceImpl(UserMapper userMapper, StringRedisTemplate stringRedisTemplate, RedissonClient redissonClient) {
        this.userMapper = userMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redissonClient = redissonClient;
    }

    //获取任务下评论
    @Override
    public HttpResult getTaskChat(String taskId) {
        //获取评论通过点赞数排列顺序
        List<TaskMessage> taskMessages = query().eq("task_id",taskId).orderByDesc("like_num").list();

        taskMessages.forEach(e -> {
            var user = userMapper.selectById(e.getUserId());
            e.setUserName(user.getNickName());
            e.setUserIcon(user.getAchUrl());
        });

        return HttpResult.success(taskMessages);
    }
    @Override
    @Transactional
    //修改信息的状态
    public HttpResult updateState(long taskMessageId,boolean isEnable){
        TaskMessage taskMessage = getById(taskMessageId);
        if (isEnable){
            taskMessage.setState(TaskMessageEnum.FALSE.code);
        }
        else{
            taskMessage.setState(TaskMessageEnum.TRUE.code);
        }
        boolean isTrue = update().update(taskMessage);
        if(!isTrue){
            return HttpResult.builder().msg("修改失败").build();
        }
        return HttpResult.builder().msg("修改成功").build();
    }

    //获取点赞数
    @Override
    public HttpResult getLike(String messageId) {
        String key = RedisConstants.MESSAGE_KEY+messageId;
        //缓存获取
        CacheClient client = new CacheClient(stringRedisTemplate, redissonClient);
        Long num = client.queryWithPassThrough(key, messageId, TaskMessage.class, id -> getById(messageId), RedisConstants.Message_TTL, TimeUnit.HOURS).getLikeNum();
        if(ObjUtil.isNull(num)){
            return HttpResult.fail("获取点赞数失败");
        }
        return HttpResult.success(num);
    }
}




