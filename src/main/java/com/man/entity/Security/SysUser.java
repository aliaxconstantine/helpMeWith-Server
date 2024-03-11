package com.man.entity.Security;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@Builder
@Schema(description = "系统用户对象")
public class SysUser {


  @TableId(value = "id", type = IdType.ASSIGN_ID)
  @Schema(description = "用户ID")
  private Long id;

  @Schema(description = "用户名")
  private String username;

  @Schema(description = "密码")
  private String password;

  @Schema(description = "是否启用")
  private Integer enabled;

  @Schema(description = "账号是否过期")
  private Integer accountNoExpired;

  @Schema(description = "凭证是否过期")
  private Integer credentialsNoExpired;

  @Schema(description = "账号是否锁定")
  private Integer accountNoLocked;

}

