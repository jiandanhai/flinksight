package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作模板DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "操作模板DTO")
public class OperationTemplateDTO  implements Serializable {
    private Long id;
    private String name;
    private String type;
    private String content;
    private Long tenantId;
    private LocalDateTime createTime;
    private Integer isDeleted;
}
