package com.HelpMe.Controller;

import com.HelpMe.Entity.core.Communication;
import com.HelpMe.mapper.CommunicationsMapper;
import com.HelpMe.utils.RedisConstants;
import com.HelpMe.utils.SystemConstants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
public class MessageScheduler {

    public final StringRedisTemplate redisTemplate;
    public final CommunicationsMapper messageMapping;

    @Autowired
    public MessageScheduler(StringRedisTemplate redisTemplate, CommunicationsMapper messageMapping) {
        this.redisTemplate = redisTemplate;
        this.messageMapping = messageMapping;
    }

    @Scheduled(cron = "0 0 2 * * ?") // 每隔50秒执行一次
    public void sendMessage() {
        String key = RedisConstants.SYSMESSAGE_KEY + System.currentTimeMillis()/ SystemConstants.SAVE_MESSAGE_TIME;
        //先获取到所有的缓存
        Set<Object> keys = redisTemplate.opsForHash().keys(key);
        if (keys.size()>0) {
            keys.forEach( (message) -> {
                messageMapping.insert((Communication)message);
            });
        }
    }
}
