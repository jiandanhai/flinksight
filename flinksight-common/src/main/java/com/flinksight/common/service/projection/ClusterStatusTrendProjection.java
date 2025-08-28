package com.flinksight.common.service.projection;

public interface ClusterStatusTrendProjection {

    String getDay();       // 日期（yyyy-MM-dd）

    String getStatus();    // 状态（如 healthy, warning, critical）

    Integer getCnt();      // 数量
}