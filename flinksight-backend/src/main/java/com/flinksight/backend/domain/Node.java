package com.flinksight.backend.domain;

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
@Getter
@Setter
@Entity
@Table(
        name = "node",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_node_cluster_name", columnNames = {"cluster_id", "name"}),
                @UniqueConstraint(name = "uk_node_ip", columnNames = {"ip"})
        },
        indexes = {
                @Index(name = "idx_node_cluster", columnList = "cluster_id"),
                @Index(name = "idx_node_status", columnList = "status")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "节点表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class Node implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "节点ID")
    private Long id;

    @Column(name = "name", nullable = false, length = 64)
    @Schema(description = "节点名称")
    private String name;

    @Column(name = "type", length = 32)
    @Schema(description = "节点类型")
    private String type;

    @Column(name = "ip", nullable = false, length = 64)
    @Schema(description = "节点IP")
    private String ip;

    @Column(name = "cluster_id", nullable = false)
    @Schema(description = "关联集群ID")
    private Long clusterId;

    @Column(name = "status", nullable = false)
    @Schema(description = "节点状态")
    private Integer status;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "注册时间")
    private LocalDateTime createTime;
}
