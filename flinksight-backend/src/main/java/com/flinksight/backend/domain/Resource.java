package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 资源管理表（支持对象存储、HDFS、S3等资源统一注册）
 */
@Getter
@Setter
@Entity
@Table(
        name = "resource",
        uniqueConstraints = @UniqueConstraint(name = "uk_resource_tenant_name", columnNames = {"tenant_id", "name"}),
        indexes = {
                @Index(name = "idx_resource_tenant_type", columnList = "tenant_id, type")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源管理表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createTime", "id"})
public class Resource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "资源ID")
    private Long id;

    @Column(name = "name", nullable = false, length = 128)
    @Schema(description = "资源名称")
    private String name;

    @Column(name = "type", length = 32)
    @Schema(description = "资源类型")
    private String type;

    @Column(name = "path", length = 256)
    @Schema(description = "资源路径/URL")
    private String path;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "归属租户ID")
    private Long tenantId;

    @Column(name = "description", length = 256)
    @Schema(description = "描述")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
