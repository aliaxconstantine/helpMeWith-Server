package com.man.entity.Security;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SysRule {

  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  private String name;

  private String code;

  private Long type;

  private Long deleteFlag;
}

