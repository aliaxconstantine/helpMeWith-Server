package com.man.entity.core;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//点赞类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户点赞消息")
public class UserLikeMessage {

  @NotNull(message = "ID不能为空")
  @Schema(description = "点赞ID", example = "1")
  private long id;

  @NotNull(message = "任务ID不能为空")
  @Schema(description = "任务ID", example = "100")
  private long taskId;

  @NotNull(message = "消息ID不能为空")
  @Schema(description = "消息ID", example = "500")
  private long messageId;

  @NotNull(message = "用户ID不能为空")
  @Schema(description = "用户ID", example = "200")
  private long userId;
}
