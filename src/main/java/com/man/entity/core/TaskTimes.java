package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;

@Data
@Builder
@Schema(description = "任务时间对象")
public class TaskTimes {

  @NotNull(message = "ID不能为空")
  @TableId(value="id", type = IdType.AUTO)
  @Schema(description = "ID", example = "1")
  private Long id;

  @NotNull(message = "创建时间不能为空")
  @Schema(description = "创建时间", example = "2022-01-01T12:00:00")
  private Timestamp createTime;

  @NotNull(message = "获取时间不能为空")
  @Schema(description = "获取时间", example = "2022-01-02T10:00:00")
  private Timestamp getTime;

  @NotNull(message = "完成时间不能为空")
  @Schema(description = "完成时间", example = "2022-01-03T15:00:00")
  private Timestamp finishTime;

  @NotNull(message = "订单ID不能为空")
  @Schema(description = "订单ID", example = "100")
  private Long taskId;

  @NotNull(message = "希望完成时间不能为空")
  @Schema(description = "希望完成时间", example = "2022-02-01T23:59:59")
  private Timestamp dueTime;
}

