package com.HelpMe.Service.impl.core;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.HelpMe.dto.HttpResult;
import com.HelpMe.dto.RecodeFrom;
import com.HelpMe.Entity.core.TRechat;
import com.HelpMe.Service.CoreService.TRechatService;
import com.HelpMe.mapper.TRechatMapper;
import com.HelpMe.utils.AuthenticationUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
* @author 艾莉希雅
* @description 针对表【t_rechat】的数据库操作Service实现
* @createDate 2023-11-25 17:09:04
*/
@Service
public class TRechatServiceImpl extends ServiceImpl<TRechatMapper, TRechat>
    implements TRechatService{

    @Override
    public HttpResult updateList(List<RecodeFrom> recodeFromList) {
        recodeFromList.forEach(recodeFrom ->{
            TRechat tRechat = TRechat.builder()
                    .userId(AuthenticationUtils.getId())
                    .tKey(recodeFrom.getKey())
                    .tValue(recodeFrom.getValue())
                    .createTime(Timestamp.valueOf(LocalDateTime.now()))
                    .build();
            boolean flag = update().update(tRechat);
            if(!flag){
                throw new RuntimeException("未知错误");
            }
        });
        return HttpResult.success(true);
    }

    @Override
    public HttpResult getAll() {
        List<TRechat> reloads = query().eq("user_id",AuthenticationUtils.getId()).list();
        List<RecodeFrom> recodeFromList = new ArrayList<>();
        reloads.forEach(r->{
            recodeFromList.add(RecodeFrom.builder()
                            .key(r.getTKey())
                            .value(r.getTValue())
                    .build());
        });
        return HttpResult.success(recodeFromList);
    }
}




