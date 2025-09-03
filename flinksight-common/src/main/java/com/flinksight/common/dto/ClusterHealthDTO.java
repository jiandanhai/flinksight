// src/main/java/com/flinksight/backend/dto/cluster/ClusterHealthDTO.java
package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "集群健康体检结果")
public class ClusterHealthDTO {

  public enum Status { UP, DEGRADED, DOWN, UNKNOWN }

  @Schema(description = "集群名")
  private String name;

  @Schema(description = "类型")
  private String type;

  @Schema(description = "endpoint")
  private String endpoint;

  @Schema(description = "健康状态")
  private Status status;

  @Schema(description = "探测耗时（毫秒）")
  private long latencyMs;

  @Schema(description = "消息摘要")
  private String message;

  @Schema(description = "关键指标采样（不同类型返回不同键，例如：flink.jobsRunning / spark.appsActive 等）")
  private Map<String, Object> samples;

  @Schema(description = "时间戳")
  private Instant checkedAt;
}
