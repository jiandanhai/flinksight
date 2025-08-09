package com.flinksight.backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 运维自动化任务实体
 * 映射数据库表 ops_task
 * 负责存储所有运维自动化任务信息，支持多租户隔离、软删除、任务状态管理等
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ops_task")
public class OpsTask implements Serializable {

    /**
     * 主键ID，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    /**
     * 租户ID（强租户隔离）
     */
    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    /**
     * 任务名称
     */
    @Column(name = "name", nullable = false, length = 128)
    private String name;

    /**
     * 任务类型（如 backup、expand、upgrade 等）
     */
    @Column(name = "type", nullable = false, length = 32)
    private String type;

    /**
     * 任务执行状态
     * 例如：PENDING、RUNNING、SUCCESS、FAILED 等
     */
    @Column(name = "status", nullable = false, length = 16)
    private String status;

    /**
     * 运维任务描述
     */
    @Column(name = "description", length = 255)
    private String description;

    /**
     * 任务创建时间
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 任务执行时间
     */
    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    /**
     * 软删除标志（0=正常 1=删除）
     */
    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted;
}