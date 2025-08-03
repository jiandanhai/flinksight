package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 用户-部门关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_department",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_department", columnNames = {"user_id", "department_id"}),
        indexes = {
                @Index(name = "idx_user_dept_user", columnList = "user_id"),
                @Index(name = "idx_user_dept_dept", columnList = "department_id")
        }
)
@Schema(description = "用户-部门关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class UserDepartment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "department_id", nullable = false)
    @Schema(description = "部门ID")
    private Long departmentId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
