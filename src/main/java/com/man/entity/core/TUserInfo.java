package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
public class TUserInfo {
  @TableId(type = IdType.AUTO)
  @Schema(description = "主键ID", example = "1")
  private Long id;

  @NotNull(message = "用户描述不能为空")
  @Size(min = 1, max = 255, message = "用户描述长度必须在1到255之间")
  @Schema(description = "用户描述", example = "这是一个用户描述")
  private String uDesc;

  @NotNull(message = "用户ID不能为空")
  @Schema(description = "用户ID", example = "1001")
  private Long userId;

  @NotNull(message = "性别不能为空")
  @Size(min = 1, max = 10, message = "性别描述长度必须在1到10之间")
  @Schema(description = "性别", example = "男")
  private String sex;

  @NotNull(message = "工作地址不能为空")
  @Size(min = 1, max = 255, message = "工作地址长度必须在1到255之间")
  @Schema(description = "工作地址", example = "北京")
  private String workAddress;

  @Schema(description = "创建时间", example = "2022-01-01T12:00:00")
  private java.sql.Timestamp createTime;

  @NotNull(message = "工作类型不能为空")
  @Size(min = 1, max = 50, message = "工作类型长度必须在1到50之间")
  @Schema(description = "工作类型", example = "全职")
  private String workType;
}