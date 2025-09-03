package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClusterDTO implements Serializable {
    // ====== 原有字段（与DB表结构一一对应） ======
    private Long id;
    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Schema(description = "集群名称")
    private String name;

    @Schema(description = "引擎 (FLINK/SPARK)")
    private String engine;

    @Schema(description = "类型 (YARN/K8S/Standalone)")
    private String type;

    @Schema(description = "K8s 命名空间；YARN/Standalone 可为空")
    private String namespace;

    @Schema(description = "集群访问地址或控制面端点")
    private String endpoint;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "标签")
    private String tags;

    private String apiEndpoint;

    @Schema(description = "ClusterSpec 快照（JSONB）")
    private String spec;

    @Schema(description = "状态：1=ENABLED,0=DISABLED")
    private Integer status = 1;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "软删除；0=未删 1=已删")
    private Integer isDeleted = 0;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;;

    // ====== 扩展字段，仅用于前端页面展示，不在DB持久化 ======

    /**
     * 集群健康状态
     * 如HEALTHY/WARNING/ERROR等，通常通过聚合 cluster_status_history 或 node_health 统计获得
     * 该字段不落数据库，仅在后端接口聚合时动态赋值，用于前端业务展示
     */
    private String healthStatus;

    /**
     * 集群下活跃节点数
     * 统计型字段，通过关联 node/cluster_status_history 后端聚合
     * 非DB字段，仅前端展示
     */
    private Integer activeNodeCount;

    /**
     * 该集群下关联的任务总数
     * 通过任务表job、job_instance等聚合统计
     * 非DB字段，仅前端展示
     */
    private Integer jobCount;
}