package com.HelpMe.Entity.core;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

@Data
@Builder
@Tag(name="订单类别")
public class TaskCategory {
    @NotNull(message = "ID不能为空")
    @TableId(value="id", type = IdType.AUTO)
    @Schema(description = "类别ID", example = "1")
    private Long id;

    @NotNull(message = "类别名称不能为空")
    @Schema(description = "类别名称", example = "电子产品")
    private String category;

    @NotNull(message = "任务大类不能为空")
    @Schema(description = "任务大类", example = "数码产品")
    private String bigClass;

    @NotNull(message = "热度不能为空")
    @Schema(description = "热度", example = "10")
    private Integer hotNum;
}