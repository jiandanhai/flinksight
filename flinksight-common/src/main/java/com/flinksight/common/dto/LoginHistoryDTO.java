package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录历史DTO
 */
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "登录历史DTO")
public class LoginHistoryDTO implements Serializable {
    @Schema(description = "日志ID")
    private Long id;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "租户ID")
    private Long tenantId;
    @Schema(description = "IP")
    private String ipAddress;
    @Schema(description = "登录类型")
    private String loginType;
    @Schema(description = "设备信息")
    private String deviceInfo;
    @Schema(description = "登录时间")
    private LocalDateTime loginTime;
    @Schema(description = "是否成功")
    private Integer successFlag;
    @Schema(description = "失败原因")
    private String failReason;
    private Integer isDeleted;
}