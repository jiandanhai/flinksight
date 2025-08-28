package com.flinksight.backend.domain;

import com.flinksight.common.enums.NodeState;
import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 节点/主机/Agent表
 * 支持节点注册、集群管理
 */
@Getter @Setter
@Entity
@Table(
        name = "node",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_node_cluster_name", columnNames = {"cluster_id", "name"}),
                @UniqueConstraint(name = "uk_node_tenant_ip",     columnNames = {"tenant_id", "ip"})
        },
        indexes = {
                @Index(name = "idx_node_tenant_cluster", columnList = "tenant_id, cluster_id"),
                @Index(name = "idx_node_tenant",         columnList = "tenant_id")
        }
)
@Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "节点表")
@SQLRestriction("is_deleted=0")
@DefaultSort(fields = {"createTime", "id"})
public class Node implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "节点ID")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Column(name = "type", length = 32)
    private String type;

    @Column(name = "ip", nullable = false, length = 64)
    private String ip;

    @Column(name = "cluster_id", nullable = false)
    private Long clusterId;

    /** 启用/禁用，健康不在此字段 */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private NodeState status;

    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted = 0;

    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;
}