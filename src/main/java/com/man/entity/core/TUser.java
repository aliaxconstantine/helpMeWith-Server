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
    private Long id;
    private String loginName;
    @NotNull(message = "登录名不能为空")
    @Size(min = 1, max = 20, message = "登录名长度必须在1到20之间")
    private String nickName;

    @NotNull(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6到20之间")
    private String password;

    @Pattern(regexp = "[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+", message = "电子邮箱格式不正确")
    private String email;

    private String achUrl;

    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "1[3-9]\\d{9}", message = "手机号码格式不正确")
    private Long phone;

    private Long sysUser;
}