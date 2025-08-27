package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 集群状态采集历史表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "cluster_status_history",
        indexes = {
                @Index(name = "idx_cluster", columnList = "cluster_id"),
                @Index(name = "idx_collect_time", columnList = "collect_time")
        }
)
@Schema(description = "集群状态采集历史")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"collectTime", "id"})
public class ClusterStatusHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "cluster_id", nullable = false)
    @Schema(description = "集群ID")
    private Long clusterId;

    @Column(name = "collect_time", nullable = false)
    @Schema(description = "采集时间")
    private LocalDateTime collectTime;

    @Column(name = "active_node_count", nullable = false)
    @Schema(description = "当前活跃节点数")
    private Integer activeNodeCount;

    @Column(name = "cpu_usage")
    @Schema(description = "CPU使用率")
    private Double cpuUsage;

    @Column(name = "memory_usage")
    @Schema(description = "内存使用率")
    private Double memoryUsage;

    @Column(name = "queue_load_json", columnDefinition = "TEXT")
    @Schema(description = "队列/命名空间/队列负载(JSON存储)")
    private String queueLoadJson;

    @Column(name = "extend_json", columnDefinition = "TEXT")
    @Schema(description = "资源池扩展（JSON）")
    private String extendJson;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;
}
