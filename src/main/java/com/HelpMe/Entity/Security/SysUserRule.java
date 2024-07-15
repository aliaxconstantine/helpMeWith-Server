package com.HelpMe.Entity.Security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SysUserRule {

  private long userId;
  private long menuId;

}
