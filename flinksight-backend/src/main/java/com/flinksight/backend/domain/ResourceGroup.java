package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源分组表
 */
@Getter
@Setter
@Entity
@Table(
        name = "resource_group",
        uniqueConstraints = @UniqueConstraint(name = "uk_resourcegroup_tenant_name", columnNames = {"tenant_id", "name"}),
        indexes = {
                @Index(name = "idx_resourcegroup_tenant_type", columnList = "tenant_id, type"),
                @Index(name = "idx_resourcegroup_parent", columnList = "parent_id")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源分组表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createTime", "id"})
public class ResourceGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "分组ID")
    private Long id;

    @Column(name = "name", nullable = false, length = 128)
    @Schema(description = "分组名称")
    private String name;

    @Column(name = "type", length = 32)
    @Schema(description = "分组类型")
    private String type;

    @Column(name = "parent_id")
    @Schema(description = "父分组ID")
    private Long parentId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "description", length = 256)
    @Schema(description = "描述")
    private String description;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
