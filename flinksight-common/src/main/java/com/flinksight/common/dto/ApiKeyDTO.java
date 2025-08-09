package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API密钥DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API密钥DTO")
public class ApiKeyDTO  implements Serializable {
    private Long id;
    private String name;
    private String apiKey;
    private Long tenantId;
    private Long userId;
    private Integer status;
    private LocalDateTime expireTime;
    private LocalDateTime createTime;
    private Integer isDeleted;
}
