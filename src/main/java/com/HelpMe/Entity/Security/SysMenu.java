package com.HelpMe.Entity.Security;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SysMenu {


  @TableId(value = "id", type = IdType.AUTO)
  private Long id;
  private String name;

}
