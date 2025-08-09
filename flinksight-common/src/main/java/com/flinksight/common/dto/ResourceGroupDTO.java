package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源分组DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源分组DTO")
public class ResourceGroupDTO  implements Serializable {
    private Long id;
    private String name;
    private String type;
    private Long parentId;
    private Long tenantId;
    private String description;
    private LocalDateTime createTime;
    private Integer isDeleted;
}
