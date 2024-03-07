package com.man.dto;

import com.man.entity.core.TStar;
import com.man.entity.core.TUserInfo;
import com.man.entity.core.Task;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFrom {
    private Long userId;
    private String icon;
    private String nickName;
    private TUserInfo userInfo;
    private Task[] tasks;
    private TStar[] stars;
}
