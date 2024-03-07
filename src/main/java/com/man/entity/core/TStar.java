package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TStar {
  @TableId(type = IdType.AUTO)
  private Long id;
  private Long taskId;
  private Long userId;
  private Long star;
  private String desc;
  @TableField(exist = false)
  private String achUrl;
  @TableField(exist = false)
  private String nickName;
  @TableField(exist = false)
  private String taskName;
  @TableField(exist = false)
  private String taskUrl;

}
