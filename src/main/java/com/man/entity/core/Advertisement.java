package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;


@Data
@Builder
@Tag(name = "advertisement")
public class Advertisement {
  @TableId(value="id", type = IdType.AUTO)
  @NotNull(message = "广告ID不能为空")
  @Schema(description = "广告ID", example = "1")
  private Long id;

  @NotNull(message = "广告描述不能为空")
  @Schema(description = "广告描述")
  private String description;

  @NotNull(message = "广告图片URL不能为空")
  @Schema(description = "广告图片URL")
  private String imageUrl;

  @NotNull(message = "广告日期不能为空")
  @Schema(description = "广告日期")
  private Date date;
}