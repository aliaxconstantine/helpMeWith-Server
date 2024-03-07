package com.man.entity.core;

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

  private long id;
  private long userId;
  private String message;

}
