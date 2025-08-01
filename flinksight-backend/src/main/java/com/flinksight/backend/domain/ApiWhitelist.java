package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API白名单表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "api_whitelist",
        indexes = {
                @Index(name = "idx_api_path", columnList = "api_path"),
                @Index(name = "idx_status", columnList = "status"),
                @Index(name = "idx_tenant", columnList = "allowed_tenant_id")
        }
)
@Schema(description = "API白名单")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class ApiWhitelist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "api_path", nullable = false, length = 255)
    @Schema(description = "API路径（支持Ant风格）")
    private String apiPath;

    @Column(length = 255)
    @Schema(description = "API描述")
    private String description;

    @Column(name = "allowed_user_id")
    @Schema(description = "允许调用的用户ID")
    private Long allowedUserId;

    @Column(name = "allowed_tenant_id")
    @Schema(description = "允许调用的租户ID")
    private Long allowedTenantId;

    @Column(nullable = false)
    @Schema(description = "状态 0禁用 1启用")
    private Integer status = 1; // 默认启用

    @Column(length = 64)
    @Schema(description = "创建人")
    private String creator;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(length = 64)
    @Schema(description = "更新人")
    private String updater;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
