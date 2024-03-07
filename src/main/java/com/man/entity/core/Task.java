package com.man.entity.core;
import com.rabbitmq.utility.IntAllocator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Task", description = "任务对象")
public class Task {
    @Schema(description = "任务ID")
    private Long id;

    @Schema(description = "图片URL")
    private String imageUrl;
    @NotNull(message = "任务名称不能为空")
    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "任务状态")
    private Integer status;

    @NotNull(message = "任务描述不能为空")
    @Schema(description = "任务描述")
    private String description;

    @NotNull(message = "缺少任务发起者")
    @Schema(description = "发起者ID")
    private Long initiatorId;
    @Schema(description = "执行者ID")
    private Long assigneeId;

    @Schema(description = "任务日期")
    private Timestamp date;

    @Schema(description = "评论数")
    private Integer comments;

    @NotNull(message = "任务类型为空")
    @Schema(description = "任务类型")
    private String type;
    @Schema(description = "任务分区")
    private String bigType;
    @NotNull(message = "缺少坐标")
    @Schema(description = "坐标x")
    private Double x;
    @NotNull(message = "缺少坐标")
    @Schema(description = "坐标y")
    private Double y;

    @Schema(description = "任务进度")
    private String progress;
    @NotNull(message = "缺少任务价格")
    @Schema(description = "任务价格")
    private Long price;

    @TableField(exist = false)
    @Schema(description = "用户ID")
    private Long userId;

    @TableField(exist = false)
    @Schema(description = "用户名")
    private String userName;

    @TableField(exist = false)
    @Schema(description = "用户图标")
    private String userIcon;

    @TableField(exist = false)
    @Schema(description = "距离")
    private Double distance;
}