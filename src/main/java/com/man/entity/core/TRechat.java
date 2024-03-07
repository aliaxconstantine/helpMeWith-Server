package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//回复类
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TRechat {
  @TableId(value="id", type = IdType.AUTO)
  private Long id;
  private String tKey;
  private String tValue;
  private Long userId;
  private java.sql.Timestamp createTime;
}
