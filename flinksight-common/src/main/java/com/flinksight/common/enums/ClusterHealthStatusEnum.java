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
public enum ClusterHealthStatusEnum {
    HEALTHY(1, "健康"),
    WARNING(2, "警告"),
    ERROR(3, "异常");

    private final Integer code;
    private final String desc;

    private static final Map<Integer, ClusterHealthStatusEnum> BY_CODE;
    static {
        Map<Integer, ClusterHealthStatusEnum> m = new HashMap<>();
        for (ClusterHealthStatusEnum e : values()) m.put(e.code, e);
        BY_CODE = Collections.unmodifiableMap(m);
    }

    public static ClusterHealthStatusEnum ofCode(Integer code) {
        ClusterHealthStatusEnum e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown ClusterHealthStatusEnum code: " + code);
        return e;
    }
    public static ClusterHealthStatusEnum ofCodeOrNull(Integer code) { return BY_CODE.get(code); }
    public static ClusterHealthStatusEnum ofCodeOrDefault(Integer code, ClusterHealthStatusEnum def) {
        return BY_CODE.getOrDefault(code, def);
    }
}
