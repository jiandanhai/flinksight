package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "cluster",
        uniqueConstraints = @UniqueConstraint(name = "uk_name_tenant", columnNames = {"name", "tenant_id"}),
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_type", columnList = "type"),
                @Index(name = "idx_status", columnList = "status")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "集群表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"createdAt", "id"})
public class Cluster implements Serializable {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "集群ID")
    private Long id;


    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "所属租户ID")
    private Long tenantId;


    @Column(name = "name", nullable = false, length = 64)
    @Schema(description = "集群名称")
    private String name;


    @Column(name = "engine", length = 16)
    @Schema(description = "引擎 (FLINK/SPARK)")
    private String engine;


    @Column(name = "type", nullable = false, length = 32)
    @Schema(description = "类型 (YARN/K8S/Standalone)")
    private String type;


    @Column(name = "namespace", length = 128)
    @Schema(description = "K8s 命名空间；YARN/Standalone 可为空")
    private String namespace;


    @Column(name = "endpoint", length = 255)
    @Schema(description = "集群访问地址或控制面端点")
    private String endpoint;


    @Column(name = "version", length = 32)
    @Schema(description = "版本号")
    private String version;


    @Column(name = "tags", length = 100)
    @Schema(description = "标签")
    private String tags;


    @Column(name = "spec", columnDefinition = "jsonb")
    @Schema(description = "ClusterSpec 快照（JSONB）")
    private String spec;


    @Column(name = "status", nullable = false)
    @Schema(description = "状态：1=ENABLED,0=DISABLED")
    private Integer status = 1;


    @Column(name = "remark", length = 255)
    @Schema(description = "备注")
    private String remark;


    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除；0=未删 1=已删")
    private Integer isDeleted = 0;


    @Column(name = "created_at", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist(){ this.createdAt = LocalDateTime.now(); this.updatedAt = this.createdAt; }
    @PreUpdate
    public void preUpdate(){ this.updatedAt = LocalDateTime.now(); }
}