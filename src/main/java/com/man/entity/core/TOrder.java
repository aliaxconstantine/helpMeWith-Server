package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;


@Data
@Builder
@Schema(name = "Orders", description = "订单对象")
public class TOrder {
  @TableId(value="id", type = IdType.ASSIGN_ID)
  @Schema(description = "订单ID")
  private Long id;

  @Schema(description = "顾客ID")
  private Long customerId;

  @Schema(description = "订单日期时间")
  private Timestamp orderData;

  @Schema(description = "产品ID")
  private Long productId;

  @Schema(description = "单价")
  private Double unitPrice;

  @Schema(description = "总金额")
  private Double totalAmount;

  @Schema(description = "订单状态")
  private String status;
}
