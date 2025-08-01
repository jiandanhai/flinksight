package com.flinksight.backend.domain;

import lombok.*;
import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 作业权限表（作业-用户-权限三元组）
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "job_permission",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_job_user_permission", columnNames = {"job_id", "user_id", "permission_id"})
        },
        indexes = {
                @Index(name = "idx_jobperm_job", columnList = "job_id"),
                @Index(name = "idx_jobperm_user", columnList = "user_id"),
                @Index(name = "idx_jobperm_perm", columnList = "permission_id"),
                @Index(name = "idx_jobperm_tenant", columnList = "tenant_id")
        }
)
@Schema(description = "作业-用户-权限三元组表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class JobPermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "job_id", nullable = false)
    @Schema(description = "作业ID")
    private Long jobId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "user_id", nullable = false, length = 64)
    @Schema(description = "用户ID（业务标识）")
    private String userId;

    @Column(name = "permission_id", nullable = false)
    @Schema(description = "权限ID")
    private Long permissionId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
