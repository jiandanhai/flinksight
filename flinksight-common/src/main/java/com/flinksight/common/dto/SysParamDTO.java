package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统参数DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统参数DTO")
public class SysParamDTO  implements Serializable {
    private Long id;
    private String paramKey;
    private String paramValue;
    private String description;
    private Integer isDeleted;
    private LocalDateTime updateTime;
}
