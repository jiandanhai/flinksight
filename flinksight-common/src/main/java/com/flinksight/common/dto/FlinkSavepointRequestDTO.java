package com.flinksight.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FlinkSavepointRequestDTO {
  @NotBlank private String clusterName;  // 在租户内唯一，用于解析 REST endpoint
  @NotBlank private String jobIdHex;     // Flink JID
  private String targetDirectory;        // 可选：目标 savepoint 目录（若 Flink 配置允许）
}