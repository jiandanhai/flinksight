package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "批量更新 Job 状态请求")
public class JobBatchUpdateStatusRequestDTO {

    @NotNull(message = "tenantId 不能为空")
    @Schema(description = "租户ID", example = "1")
    private Long tenantId;

    @NotEmpty(message = "jobIds 不能为空")
    @Schema(description = "任务ID列表", example = "[101,102,103]")
    private List<@NotNull Long> jobIds;

    @NotNull(message = "status 不能为空")
    @Schema(description = "目标状态（业务自定义，例如 0=停止，1=运行，2=异常...）", example = "1")
    private Integer status;
}