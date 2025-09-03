package com.flinksight.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FlinkRestartFromLastRequestDTO {
  @NotBlank private String clusterName;
  @NotBlank private String jobName;     // 由平台定位 jar 与参数（你们的作业注册表）
  private Integer newParallelism;       // 可选：小步调整
  private boolean force;                // 是否允许无 savepoint 时直接重提（默认 false）
}