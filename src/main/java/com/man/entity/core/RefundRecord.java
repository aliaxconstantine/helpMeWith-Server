package com.man.entity.core;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;
import java.sql.Timestamp;
import io.swagger.v3.oas.annotations.media.Schema;


@Data
@Builder
@Schema(name = "RefundRecord", description = "退款记录对象")
public class RefundRecord {

  @TableId(value = "id",type = IdType.AUTO)
  @Schema(description = "退款记录ID")
  private long id;

  @Schema(description = "订单ID")
  private long orderId;

  @Schema(description = "退款金额")
  private double amount;

  @Schema(description = "退款状态")
  private String status;

  @Schema(description = "创建时间")
  private Timestamp createdAt;

  @Schema(description = "更新时间")
  private Timestamp updatedAt;
}
