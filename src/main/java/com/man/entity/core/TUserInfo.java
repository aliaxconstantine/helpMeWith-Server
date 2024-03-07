package com.man.entity.core;

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
public class TUserInfo {
  @TableId(type = IdType.AUTO)
  private Long id;
  private String uDesc;
  private Long userId;
  private String sex;
  private String workAddress;
  private java.sql.Timestamp createTime;
  private String workType;
}
