package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "节点健康DTO")
public class NodeHealthDTO  implements Serializable {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "节点ID")
    private Long nodeId;

    @Schema(description = "健康状态（如 HEALTHY/UNHEALTHY/WARNING）")
    private String healthStatus;

    @Schema(description = "健康检测时间")
    private LocalDateTime checkTime;

    @Schema(description = "状态描述")
    private String message;

    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
