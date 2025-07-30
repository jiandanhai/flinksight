package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登录历史DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录历史DTO")
public class LoginHistoryDTO {
    private Long id;
    private Long userId;
    private String ip;
    private String userAgent;
    private LocalDateTime loginTime;
    private Integer isDeleted;
}
