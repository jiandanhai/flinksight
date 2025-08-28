package com.flinksight.common.service.projection;

import com.flinksight.common.enums.NodeState;

import java.time.LocalDateTime;

/**
 * 列表行投影（比 DTO 更轻，JPA 直接映射结果集）原生 SQL + 相关子查询拿“每个节点的最新健康”
 */
public interface NodeListRow {
    Long getId();
    Long getTenantId();
    Long getClusterId();
    String getName();
    String getType();
    String getIp();
    NodeState getStatus();          // 启用/禁用
    String getHealth();             // HEALTHY/WARNING/UNHEALTHY/NULL
    LocalDateTime getCreateTime();
    String getHealthMessage();
    LocalDateTime getHealthTime();
}