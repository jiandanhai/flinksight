package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 消息通知DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "消息通知DTO")
public class NotificationDTO {
    private Long id;
    private String title;
    private String content;
    private Integer type;
    private Long userId;
    private Integer status;
    private Long tenantId;
    private LocalDateTime sendTime;
    private Integer isDeleted;
}
