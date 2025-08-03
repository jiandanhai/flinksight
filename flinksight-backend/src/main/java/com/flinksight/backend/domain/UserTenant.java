package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

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
@SQLRestriction("is_deleted=0") // Hibernate 6.3 推荐软删注解
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

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
