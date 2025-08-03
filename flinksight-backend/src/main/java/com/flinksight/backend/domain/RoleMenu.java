package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 角色-菜单关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "role_menu",
        uniqueConstraints = @UniqueConstraint(name = "uk_role_menu", columnNames = {"role_id", "menu_id"}),
        indexes = {
                @Index(name = "idx_role_menu_role", columnList = "role_id"),
                @Index(name = "idx_role_menu_menu", columnList = "menu_id")
        }
)
@Schema(description = "角色-菜单关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class RoleMenu implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "role_id", nullable = false)
    @Schema(description = "角色ID")
    private Long roleId;

    @Column(name = "menu_id", nullable = false)
    @Schema(description = "菜单ID")
    private Long menuId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
