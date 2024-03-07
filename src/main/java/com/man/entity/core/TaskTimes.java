package com.man.entity.core;

import lombok.Builder;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import java.sql.Timestamp;

@Data
@Builder
@Schema(description = "任务时间对象")
public class TaskTimes {

  @Schema(description = "ID")
  private Long id;

  @Schema(description = "创建时间")
  private Timestamp createTime;

  @Schema(description = "获取时间")
  private Timestamp getTime;

  @Schema(description = "完成时间")
  private Timestamp finishTime;
  @Schema(description = "订单id")
  private Long taskId;
  @Schema(description = "希望完成时间")
  private Timestamp dueTime;
}


