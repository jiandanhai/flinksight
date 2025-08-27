package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户-租户关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_tenant",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_tenant", columnNames = {"user_id", "tenant_id"}),
        indexes = {
                @Index(name = "idx_user_tenant_user", columnList = "user_id"),
                @Index(name = "idx_user_tenant_tenant", columnList = "tenant_id")
        }
)
@Schema(description = "用户-租户关联表")
@SQLRestriction("is_deleted=0") // 软删除
@DefaultSort(fields = {"createTime", "id"})
public class UserTenant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "role", length = 32)
    @Schema(description = "授权角色，如OWNER/ADMIN/USER")
    private String role;

    @Column(name = "is_default", nullable = false)
    @Schema(description = "是否默认租户（1=默认，0=非默认）")
    private Integer isDefault = 0;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time", nullable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
