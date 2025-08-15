package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 集群健康状态枚举
 */
@Getter
@AllArgsConstructor
public enum ClusterHealthStatusEnum {
    HEALTHY(1, "健康"),
    WARNING(2, "警告"),
    ERROR(3, "异常");

    private final Integer code;
    private final String desc;
}
