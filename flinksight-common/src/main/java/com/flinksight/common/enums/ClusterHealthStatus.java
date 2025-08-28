package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 集群健康状态枚举
 */
@Getter
@AllArgsConstructor
public enum ClusterHealthStatus {
    HEALTHY(1, "健康"),
    WARNING(2, "警告"),
    ERROR(3, "异常");

    private final Integer code;
    private final String desc;

    private static final Map<Integer, ClusterHealthStatus> BY_CODE;
    static {
        Map<Integer, ClusterHealthStatus> m = new HashMap<>();
        for (ClusterHealthStatus e : values()) m.put(e.code, e);
        BY_CODE = Collections.unmodifiableMap(m);
    }

    public static ClusterHealthStatus ofCode(Integer code) {
        ClusterHealthStatus e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown ClusterHealthStatusEnum code: " + code);
        return e;
    }
    public static ClusterHealthStatus ofCodeOrNull(Integer code) { return BY_CODE.get(code); }
    public static ClusterHealthStatus ofCodeOrDefault(Integer code, ClusterHealthStatus def) {
        return BY_CODE.getOrDefault(code, def);
    }
}
