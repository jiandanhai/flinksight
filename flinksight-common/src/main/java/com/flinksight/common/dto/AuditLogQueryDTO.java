package com.flinksight.common.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class AuditLogQueryDTO {
    private Long tenantId;                 // 必填（多租户隔离）
    private String action;                 // 可选：过滤某操作类型
    private String targetType;             // 可选
    private Long targetId;                 // 可选
    private Long userId;                   // 可选
    private String operator;               // 可选
    private String traceId;                // 可选

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime from;            // 可选

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime to;              // 可选

    private Integer page = 1;              // 1-based
    private Integer size = 20;
}