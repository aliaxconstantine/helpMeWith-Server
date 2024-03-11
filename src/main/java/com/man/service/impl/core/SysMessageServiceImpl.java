package com.man.service.impl.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.man.dto.HttpResult;
import com.man.entity.core.SysMessage;
import com.man.entity.core.TUser;
import com.man.mapper.TUserMapper;
import com.man.service.CoreService.SysMessageService;
import com.man.mapper.SysMessageMapper;
import com.man.utils.AuthenticationUtils;
import com.man.utils.RedisConstants;
import com.man.utils.SystemConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【sys_message】的数据库操作Service实现
* @createDate 2024-03-04 16:45:50
*/
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessage>
    implements SysMessageService{
    private final TUserMapper tUserMapper;
    private final StringRedisTemplate stringRedisTemplate;
    @Autowired
    public SysMessageServiceImpl(TUserMapper tUserMapper, StringRedisTemplate stringRedisTemplate) {
        this.tUserMapper = tUserMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void sendMessage(String userId, String message) {
        //收到消息验证user
        TUser tUser = tUserMapper.selectById(userId);
        Long id = Convert.toLong(userId);
        if(ObjUtil.isNotNull(tUser) && ObjUtil.isNotNull(id)){
            //存储消息到数据库
            SysMessage sysMessage = SysMessage.builder()
                    .userId(id)
                    .message(message).build();
            //先缓存
            String key = RedisConstants.SYSMESSAGE_KEY+userId;
            stringRedisTemplate.opsForValue().set(key,message);
            save(sysMessage);
        }
    }

    @Override
    public HttpResult getAllMessage(Long pageNum) {
        //获取所有信息
        List<SysMessage> list = query().eq("user_id", AuthenticationUtils.getId()).page(new Page<>(pageNum, SystemConstants.MAX_PAGE_SIZE_MESSAGE)).getRecords();
        return HttpResult.success(list);
    }
}




