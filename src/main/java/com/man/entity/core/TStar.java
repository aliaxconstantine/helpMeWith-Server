package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "评星实体")
public class TStar {
  @TableId(type = IdType.AUTO)
  @Schema(description = "评星ID", example = "1")
  private Long id;

  @NotNull(message = "任务ID不能为空")
  @Schema(description = "任务ID", example = "100")
  private Long taskId;

  @NotNull(message = "用户ID不能为空")
  @Schema(description = "用户ID", example = "200")
  private Long userId;

  @NotNull(message = "星级不能为空")
  @Min(value = 1, message = "星级最小为1")
  @Schema(description = "星级评分", example = "5")
  private Long star;

  @Size(max = 255, message = "描述长度不能超过255个字符")
  @Schema(description = "评星描述", example = "非常满意")
  private String desc;

  @TableField(exist = false)
  @Schema(description = "成就URL", example = "http://example.com/ach.png")
  private String achUrl;

  @TableField(exist = false)
  @Schema(description = "用户昵称", example = "nickname")
  private String nickName;

  @TableField(exist = false)
  @Schema(description = "任务名称", example = "任务一")
  private String taskName;

  @TableField(exist = false)
  @Schema(description = "任务URL", example = "http://example.com/task1")
  private String taskUrl;
}
