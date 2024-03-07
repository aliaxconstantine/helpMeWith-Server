package com.man.entity.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//点赞类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeMessage {

  private long id;
  private long taskId;
  private long messageId;
  private long userId;

}
