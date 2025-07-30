package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 角色-数据权限范围关联表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_data_scope", uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "data_scope_id"}))
@Schema(description = "角色-数据权限范围关联表")
@Where(clause = "is_deleted=0")
public class RoleDataScope implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "role_id", nullable = false)
    @Schema(description = "角色ID")
    private Long roleId;

    @Column(name = "data_scope_id", nullable = false)
    @Schema(description = "数据范围ID")
    private Long dataScopeId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
