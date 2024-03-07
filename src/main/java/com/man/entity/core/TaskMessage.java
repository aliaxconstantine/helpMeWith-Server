package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@NoArgsConstructor
@Schema(description = "任务消息对象")
public class TaskMessage {
  @TableId(type = IdType.AUTO)
  @Schema(description = "消息ID", example = "1")
  private Long id;

  @Schema(description = "消息内容")
  private String message;

  @NotNull(message = "用户ID不能为空")
  @Schema(description = "用户ID", example = "1")
  private Long userId;

  @NotNull(message = "任务ID不能为空")
  @Schema(description = "任务ID", example = "1")
  private Long taskId;

  @Schema(description = "消息状态")
  private Integer state;

  @Schema(description = "创建时间")
  private Timestamp createTime;

  @Schema(description = "点赞数")
  private Long likeNum;

  @TableField(exist = false)
  @Schema(description = "用户名")
  private String userName;

  @TableField(exist = false)
  @Schema(description = "用户图标")
  private String userIcon;
}