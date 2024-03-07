package com.man.entity.core;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.NoArgsConstructor;

@Data
@Builder

@Tag(name="订单类别")
public class TaskCategory {
    @TableId(value="id", type = IdType.AUTO)
    private Long id;
    @Schema(description = "类别名称")
    private String category;
    @Schema(description = "任务大类")
    private String bigClass;
    @Schema(description = "热度")
    private Integer hotNum;
}

