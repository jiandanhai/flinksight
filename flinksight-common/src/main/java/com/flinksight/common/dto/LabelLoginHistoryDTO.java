package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

/**
 * 标签登录历史DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签登录历史DTO")
public class LabelLoginHistoryDTO {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "标签ID")
    private Long labelId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "登录IP地址")
    private String ipAddress;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "软删除 0正常 1删除")
    private Integer isDeleted;
}
