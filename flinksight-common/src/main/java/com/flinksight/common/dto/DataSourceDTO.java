package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据源DTO")
public class DataSourceDTO  implements Serializable {
    private Long id;
    private String name;
    private String type;
    private String connectInfo;
    private Long tenantId;
    private String description;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
