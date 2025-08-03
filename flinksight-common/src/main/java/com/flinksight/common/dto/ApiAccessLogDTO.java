package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * API访问日志DTO
 */
@Data
@Schema(description = "API访问日志DTO")
public class ApiAccessLogDTO {
    private Long id;
    private String url;
    private String httpMethod;
    private String params;
    private Integer status;
    private Long userId;
    private Long tenantId;
    private String ip;
    private LocalDateTime accessTime;
    private Long duration;
    private Integer isDeleted;
}
