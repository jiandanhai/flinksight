package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 节点健康状态表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "node_health")
@Schema(description = "节点健康状态表")
@Where(clause = "is_deleted=0")
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

    @Column(name = "health_status", nullable = false)
    @Schema(description = "健康状态（如 HEALTHY/UNHEALTHY/WARNING）")
    private String healthStatus;

    @Column(name = "check_time", nullable = false)
    @Schema(description = "健康检测时间")
    private LocalDateTime checkTime;

    @Column(name = "message")
    @Schema(description = "状态描述")
    private String message;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
