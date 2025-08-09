package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 作业运行实例DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "作业运行实例DTO")
public class JobInstanceDTO  implements Serializable {
    private Long id;
    private Long jobId;
    private String batchNo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private String logContent;
    private Long tenantId;
    private Integer isDeleted;
}
