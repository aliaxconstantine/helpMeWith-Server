package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;


@Data
@Builder
@Schema(name = "Communication", description = "用户好友消息对象")
public class Communication {

  @Schema(description = "通信ID")
  @TableId(value="id", type = IdType.AUTO)
  private Long id;

  @NotNull(message = "接收者用户ID不能为空")
  @Schema(description = "接收者用户ID")
  private Long receiverUserId;

  @NotNull(message = "发送者用户ID不能为空")
  @Schema(description = "发送者用户ID")
  private Long senderUserId;

  @NotNull(message = "通信内容不能为空")
  @Schema(description = "通信内容")
  private String content;

  @NotNull(message = "通信日期时间不能为空")
  @Schema(description = "通信日期时间")
  private Timestamp date;
}
