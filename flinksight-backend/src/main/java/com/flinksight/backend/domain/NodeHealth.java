package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 节点健康状态表
 * 支持运维监控、故障诊断、统计分析
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "node_health",
        indexes = {
                @Index(name = "idx_nh_tenant_node_time", columnList = "tenant_id, node_id, check_time"),
                @Index(name = "idx_nh_health_status", columnList = "health_status")
        }
)
@Schema(description = "节点健康状态表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class NodeHealth implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "node_id", nullable = false)
    @Schema(description = "节点ID")
    private Long nodeId;

    @Column(name = "health_status", nullable = false, length = 16)
    @Schema(description = "健康状态（HEALTHY/UNHEALTHY/WARNING）")
    private String healthStatus;

    @Column(name = "check_time", nullable = false)
    @Schema(description = "健康检测时间")
    private LocalDateTime checkTime;

    @Column(name = "message", length = 256)
    @Schema(description = "状态描述")
    private String message;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0=正常 1=删除")
    private Integer isDeleted = 0;
}
