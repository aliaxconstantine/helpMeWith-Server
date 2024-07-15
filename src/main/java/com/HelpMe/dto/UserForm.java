package com.HelpMe.dto;

import com.HelpMe.Entity.core.TStar;
import com.HelpMe.Entity.core.TUserInfo;
import com.HelpMe.Entity.core.Task;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserForm {
    private Long userId;
    private String icon;
    private String nickName;
    private TUserInfo userInfo;
    private Task[] tasks;
    private TStar[] stars;
}
