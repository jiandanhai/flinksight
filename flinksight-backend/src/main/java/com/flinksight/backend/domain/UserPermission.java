package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 用户-权限关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_permission",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_permission", columnNames = {"user_id", "permission_id"}),
        indexes = {
                @Index(name = "idx_user_permission_user", columnList = "user_id"),
                @Index(name = "idx_user_permission_permission", columnList = "permission_id")
        }
)
@Schema(description = "用户-权限关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class UserPermission implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "permission_id", nullable = false)
    @Schema(description = "权限ID")
    private Long permissionId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
