package com.man.utils;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author 艾莉希雅
 */
@Slf4j
@Component
public class CacheClient {
    private final StringRedisTemplate stringRedisTemplate;
    private static final ExecutorService CACHE_REBUILD_EXECUTOR = Executors.newFixedThreadPool(10);

    public CacheClient(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    //不同过期时间

    public void set(String key, Object value, Long time, TimeUnit timeUnit){
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value),time,timeUnit);
    }
    //逻辑过期
    public void setWithLogicalExpire(String key, Object value, Long time, TimeUnit timeUnit){
        //设置逻辑过期
        RedisData redisData = new RedisData();
        redisData.setData(value);
        redisData.setExpireTime(LocalDateTime.now().plusSeconds(timeUnit.toSeconds(time)));
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(value));
    }
    //缓存穿透
    public <R,ID> R queryWithPassThrough(String keyPrefix, ID id, Class<R> type, Function<ID,R> dbFallback,Long time,TimeUnit unit){
        //设置缓存查询的key
        String key = keyPrefix + id;
        //获取根据key得到的数据
        String json = stringRedisTemplate.opsForValue().get(key);
        //如果字符不是空，查询到数据，就返回根据json创建的对象
        if(StrUtil.isNotBlank(json)){
            return JSONUtil.toBean(json,type);
        }
        //如果只是空格就返回null
        if(json != null){
            return null;
        }
        //数据库查询
        R r = dbFallback.apply(id);
        //如果查询到为null，则存空字符
        if(r == null){
            stringRedisTemplate.opsForValue().set(key,"", RedisConstants.CACHE_NULL_TTL,TimeUnit.MINUTES);
            return null;
        }
        //查询到则存储数据
        this.set(key,r,time,unit);
        return r;
    }
    //缓存击穿
    public <R,ID> R queryWithMutex(String keyPrefix,ID id,Class<R> type,Function<ID,R>dbFallBack,Long time,TimeUnit unit){
        R r = null;
        String key = RedisConstants.CACHE_TASK_KEY + id;
        //从redis里查询商户缓存
        String taskJson = stringRedisTemplate.opsForValue().get(key);
        //判断是否存在
        if(StrUtil.isNotBlank((taskJson))){
            return null;
        }
        RedisData redisData = JSONUtil.toBean(taskJson, RedisData.class);
        JSONObject data = (JSONObject)redisData.getData();
        r = JSONUtil.toBean(data,type);
        LocalDateTime expireTime = redisData.getExpireTime();
        //判断是否过期
        if(expireTime.isAfter(LocalDateTime.now())){
            //未过期
            return r;
        }
        //已经过期
        //获取互斥锁id
        String lockKey = RedisConstants.LOCK_TASK_KEY+id;
        //实现缓存重建
        boolean isLock = tryLock(lockKey);
        if (isLock) {
            CACHE_REBUILD_EXECUTOR.submit(()->{
                try{
                    //查数据库，写入redis
                    R r1 = dbFallBack.apply(id);
                    this.setWithLogicalExpire(key,r1,time,TimeUnit.MINUTES);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    unLock(lockKey);
                }
            });
        }
        return r;
    }
    private boolean tryLock(String key){
        var flag = stringRedisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    private void unLock(String key){
        stringRedisTemplate.delete(key);
    }

}
