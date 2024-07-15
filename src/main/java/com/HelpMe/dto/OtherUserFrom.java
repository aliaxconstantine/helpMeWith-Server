package com.HelpMe.dto;

import com.HelpMe.Entity.core.TUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtherUserFrom {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "登录名")
    private String loginName;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "电子邮箱")
    private String email;
    @Schema(description = "头像URL")
    private String achUrl;
    private TUserInfo tUserInfo;
}
