package com.man.service.impl.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.dto.HttpResult;
import com.man.entity.core.TaskMessage;
import com.man.entity.core.UserLikeMessage;
import com.man.service.CoreService.UserLikeMessageService;
import com.man.mapper.UserLikeMessageMapper;
import com.man.utils.AuthenticationUtils;
import com.man.utils.CacheClient;
import com.man.utils.RedisConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author 艾莉希雅
* @description 针对表【user_like_message】的数据库操作Service实现
* @createDate 2024-03-03 16:38:29
*/
@Service
public class UserLikeMessageServiceImpl extends ServiceImpl<UserLikeMessageMapper, UserLikeMessage>
    implements UserLikeMessageService{
    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    public UserLikeMessageServiceImpl(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public HttpResult addLike(String messageId) {
        String key = RedisConstants.MESSAGE_KEY+messageId+AuthenticationUtils.getId();
        //检测该用户有没有点赞
        String message = stringRedisTemplate.opsForValue().get(key);
        //如果点赞了，则返回不能再次点赞
        if(StrUtil.isNotBlank(message) && message.equals("1")){
            return HttpResult.fail("您已经赞过了!");
        }
        //如果没点赞则存入数据库
        //缓存点赞记录，设置定时任务统计点赞数
        stringRedisTemplate.opsForValue().set(key,"1");
        //记录点赞数
        stringRedisTemplate.opsForValue().increment(RedisConstants.MESSAGE_KEY);
        return HttpResult.success("点赞成功");
    }

    @Override
    public HttpResult delLike(String messageId) {
        String key = RedisConstants.MESSAGE_KEY+messageId+AuthenticationUtils.getId();
        //更新缓存
        stringRedisTemplate.opsForValue().set(key,"0");
        //记录点赞数
        stringRedisTemplate.opsForValue().decrement(RedisConstants.MESSAGE_KEY);
        //缓存
        return HttpResult.success("取消点赞成功");
    }

    @Override
    public HttpResult getOk(String messageId) {
        String key = RedisConstants.MESSAGE_KEY+messageId;
        String flag = stringRedisTemplate.opsForValue().get(key);
        //0为没有点赞，1为点赞了
        if(StrUtil.isBlank(flag)){
            return HttpResult.success(0);
        }
        return HttpResult.success(flag);
    }
}




