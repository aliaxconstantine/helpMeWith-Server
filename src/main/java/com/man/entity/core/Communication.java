package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "接收者用户ID")
  private Long receiverUserId;

  @Schema(description = "发送者用户ID")
  private Long senderUserId;

  @Schema(description = "通信内容")
  private String content;

  @Schema(description = "通信日期时间")
  private Timestamp data;

}
