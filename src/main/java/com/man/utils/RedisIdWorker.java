package com.man.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class RedisIdWorker {
    private static final long BEGIN_TIMESTAMP = 16409955200L;
    private static final short COUNT_BITS = 32;
    private StringRedisTemplate stringRedisTemplate;
    public long nextId(String keyPrefix){
        //生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;
        //获取到天日期
        String data = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        //自增长
        //生成序列号
        long count = stringRedisTemplate.opsForValue().increment("icr:"+keyPrefix+":"+data);
        //拼接返回‘
        return timestamp<<COUNT_BITS | count;
    }
}
