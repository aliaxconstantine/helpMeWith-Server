package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@NoArgsConstructor
@Schema(description = "用户对象")
public class TUser {

    @TableId(value ="id", type = IdType.AUTO)
    @Schema(description = "用户ID", example = "1")
    private Long id;

    @NotNull(message = "登录名不能为空")
    @Size(min = 1, max = 20, message = "登录名长度必须在1到20之间")
    @Schema(description = "登录名", required = true, example = "user123")
    private String loginName;

    @NotNull(message = "昵称不能为空")
    @Size(min = 1, max = 20, message = "昵称长度必须在1到20之间")
    @Schema(description = "用户昵称", required = true, example = "nickname")
    private String nickName;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    @Schema(description = "用户密码", required = true, example = "password123")
    private String password;

    @Pattern(regexp = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+", message = "电子邮箱格式不正确")
    @Schema(description = "用户邮箱", example = "user@example.com")
    private String email;

    @Schema(description = "成就图片URL", example = "http://example.com/ach.png")
    private String achUrl;

    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "1[3-9]\\d{9}", message = "手机号码格式不正确")
    @Schema(description = "用户手机号码", required = true, example = "13912345678")
    private Long phone;

    @Schema(description = "系统用户ID", example = "2")
    private Long sysUser;
}