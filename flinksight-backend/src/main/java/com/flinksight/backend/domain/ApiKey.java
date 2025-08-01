package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API密钥/Token表
 */
@Getter
@Setter
@Entity
@Table(
        name = "api_key",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_api_key", columnNames = {"api_key"}),
        },
        indexes = {
                @Index(name = "idx_tenant_user", columnList = "tenant_id, user_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API密钥表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class ApiKey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "密钥ID")
    private Long id;

    @Column(nullable = false, length = 128)
    @Schema(description = "密钥名称")
    private String name;

    @Column(name = "api_key", nullable = false, unique = true, length = 256)
    @Schema(description = "API密钥内容")
    private String apiKey;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "关联用户ID")
    private Long userId;

    @Column(nullable = false)
    @Schema(description = "密钥状态 0正常 1禁用")
    private Integer status = 0;

    @Column(name = "expire_time")
    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
