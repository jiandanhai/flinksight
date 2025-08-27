package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户-角色关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_role",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_role", columnNames = {"user_id", "role_id"}),
        indexes = {
                @Index(name = "idx_user_role_user", columnList = "user_id"),
                @Index(name = "idx_user_role_role", columnList = "role_id"),
                @Index(name = "idx_user_role_tenant", columnList = "tenant_id")
        }
)
@Schema(description = "用户-角色关联表")
@SQLRestriction("is_deleted=0")
@DefaultSort(fields = {"assignTime", "id"})
public class UserRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "role_id", nullable = false)
    @Schema(description = "角色ID")
    private Long roleId;

    @Column(name = "tenant_id")
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "assign_time")
    @Schema(description = "分配时间")
    private LocalDateTime assignTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
