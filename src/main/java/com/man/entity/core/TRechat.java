package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
//回复类


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Rechat实体")
public class TRechat {
  @TableId(value = "id", type = IdType.AUTO)
  @Schema(description = "主键ID", example = "1")
  private Long id;

  @Schema(description = "键", example = "key")
  private String tKey;

  @Schema(description = "值", example = "value")
  private String tValue;

  @NotNull(message = "用户ID不能为空")
  @Schema(description = "用户ID", example = "1001")
  private Long userId;

  @Schema(description = "创建时间", example = "2022-01-01T12:00:00")
  private java.sql.Timestamp createTime;
}

