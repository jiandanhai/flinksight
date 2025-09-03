// src/main/java/com/flinksight/backend/dto/cluster/ClusterSpec.java
package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "注册集群的请求参数")
public class ClusterSpecDTO {

  @NotBlank @Size(max = 64)
  @Schema(description = "集群名称（在租户内唯一）")
  private String name;

  @NotBlank @Pattern(regexp = "FLINK|SPARK|YARN|K8S|STANDALONE")
  @Schema(description = "集群类型：FLINK / SPARK / YARN / K8S / STANDALONE")
  private String type;

  @NotBlank @Size(max = 256)
  @Schema(description = "访问端点，如 http://flink-rest:8081 或 http://yarn-rm:8088")
  private String endpoint;

  @Size(max = 32)
  @Schema(description = "版本号（可留空，注册时将自动探测）")
  private String version;

  @Size(max = 100)
  @Schema(description = "标签，逗号分隔")
  private String tags;

  @Size(max = 255)
  @Schema(description = "备注")
  private String remark;

  @Schema(description = "可选的附加配置（如认证/命名空间），按类型扩展")
  private Map<String, String> extras;
}
