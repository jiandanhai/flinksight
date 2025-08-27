package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 组织-角色关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "group_role",
        uniqueConstraints = @UniqueConstraint(name = "uk_group_role", columnNames = {"group_id", "role_id"}),
        indexes = {
                @Index(name = "idx_group", columnList = "group_id"),
                @Index(name = "idx_role", columnList = "role_id")
        }
)
@Schema(description = "组织-角色关联表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"groupId", "id"})
public class GroupRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "group_id", nullable = false)
    @Schema(description = "组织ID")
    private Long groupId;

    @Column(name = "role_id", nullable = false)
    @Schema(description = "角色ID")
    private Long roleId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0; // 默认值
}
