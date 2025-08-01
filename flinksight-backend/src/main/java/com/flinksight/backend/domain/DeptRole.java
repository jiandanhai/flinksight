package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 部门-角色关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "dept_role",
        uniqueConstraints = @UniqueConstraint(name = "uk_dept_role", columnNames = {"dept_id", "role_id"}),
        indexes = {
                @Index(name = "idx_dept", columnList = "dept_id"),
                @Index(name = "idx_role", columnList = "role_id")
        }
)
@Schema(description = "部门-角色关联表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class DeptRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "dept_id", nullable = false)
    @Schema(description = "部门ID")
    private Long deptId;

    @Column(name = "role_id", nullable = false)
    @Schema(description = "角色ID")
    private Long roleId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0; // 默认正常
}
