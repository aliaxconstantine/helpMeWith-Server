package com.HelpMe.Entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//系统消息

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysMessage {
  @TableId(value="id", type = IdType.AUTO)
  @NotNull(message = "ID不能为空")
  private long id;

  @NotNull(message = "用户ID不能为空")
  private long userId;

  @NotNull(message = "消息内容不能为空")
  private String message;
}