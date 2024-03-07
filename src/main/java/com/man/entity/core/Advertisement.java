package com.man.entity.core;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.sql.Date;


@Data
@Builder
@Tag(name = "advertisement")
public class Advertisement {


  @TableId(value="id", type = IdType.AUTO)
  @Schema(description = "广告ID")
  private Long id;

  @Schema(description = "广告描述")
  private String description;

  @Schema(description = "广告图片URL")
  private String imageUrl;

  @Schema(description = "广告日期")
  private Date date;

}