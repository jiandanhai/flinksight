package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知渠道DTO
 * 用于配置、编辑和展示通知方式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知渠道DTO")
public class NotifyChannelDTO implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "渠道类型（email/dingding/wechat/sms等）")
    private String type;

    @Schema(description = "配置内容(JSON字符串)")
    private String config;

    @Schema(description = "渠道名称")
    private String name;

    @Schema(description = "是否启用 0=禁用 1=启用")
    private Integer enabled;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "是否删除 0=正常 1=删除")
    private Integer isDeleted;
}
