package com.HelpMe.Entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Tag(name="用户好友")
public class Friend {
  @Schema(description = "用户id")
  private Long userId;
  @Schema(description = "好友id")
  private Long friendId;
  @Schema(description = "状态")
  private Integer status;
  @Schema(description = "添加好友时间")
  private Timestamp createTime;
  @TableId(value="id", type = IdType.AUTO)
  private Long id;
  @TableField(exist = false)
  @Schema(description = "用户名")
  private String userName;

  @TableField(exist = false)
  @Schema(description = "用户图标")
  private String userIcon;


}

