package com.HelpMe.Entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskImages {

  @TableId(value="id", type = IdType.AUTO)
  private long id;
  private long taskId;
  private String imageUrl;
}
