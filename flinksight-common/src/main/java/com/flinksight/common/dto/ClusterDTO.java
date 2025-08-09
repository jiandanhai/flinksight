package com.flinksight.common.dto;

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
    private Long tenantId;
    private String name;
    private String type; // YARN/K8S/Standalone
    private String endpoint;
    private String version;
    private String tags;
    private String apiEndpoint;
    private Integer status;
    private String remark;
    private Integer isDeleted;
    private LocalDateTime createTime;

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