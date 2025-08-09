package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源DTO")
public class ResourceDTO  implements Serializable {
    private Long id;
    private String name;
    private String type;
    private String uri;
    private Long size;
    private Long tenantId;
    private Long ownerId;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
