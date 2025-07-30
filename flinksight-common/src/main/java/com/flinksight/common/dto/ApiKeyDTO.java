package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * API密钥DTO
 */
@Data
@Schema(description = "API密钥DTO")
public class ApiKeyDTO {
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
