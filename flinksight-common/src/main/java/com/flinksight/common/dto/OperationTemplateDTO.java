// src/main/java/com/flinksight/common/dto/OperationTemplateDTO.java
package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@Schema(description = "操作模板 DTO")
public class OperationTemplateDTO implements Serializable {

    @Schema(description = "模板ID")
    private Long id;

    @NotBlank
    @Size(max = 128)
    @Schema(description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank
    @Size(max = 32)
    @Schema(description = "模板类型（如 SHELL/HTTP/SQL/K8S…）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String type;

    @NotBlank
    @Schema(description = "模板内容（支持 ${var} 占位符）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String content;

    @Schema(description = "租户ID（后端自动填充）")
    private Long tenantId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "软删除 0=正常 1=删除")
    private Integer isDeleted;
}
