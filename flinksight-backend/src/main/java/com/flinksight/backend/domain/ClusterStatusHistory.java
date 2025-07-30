package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 集群状态采集历史表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cluster_status_history")
@Schema(description = "集群状态采集历史")
@Where(clause = "is_deleted=0")
public class ClusterStatusHistory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clusterId;

    @Schema(description = "采集时间")
    private LocalDateTime collectTime;

    @Column(name = "active_node_count", nullable = false)
    @Schema(description = "当前活跃节点数")
    private Integer activeNodeCount;

    @Schema(description = "CPU使用率")
    private Double cpuUsage;

    @Schema(description = "内存使用率")
    private Double memoryUsage;

    @Schema(description = "队列/命名空间/队列负载")
    private String queueLoadJson;

    @Schema(description = "资源池扩展（可JSON存）")
    private String extendJson;

    @Schema(description = "是否删除")
    private Integer isDeleted;

}
