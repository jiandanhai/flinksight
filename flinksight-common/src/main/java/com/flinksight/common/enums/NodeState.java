package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 集群节点-枚举（启用/禁用）
 */
@Getter
@AllArgsConstructor
public enum NodeState {
    ENABLED("0", "启用"),
    DISABLED("1", "禁用");

    private final String code;
    private final String desc;

    private static final Map<String, NodeState> BY_CODE;
    static {
        Map<String, NodeState> m = new HashMap<>();
        for (NodeState e : values()) m.put(e.code, e);
        BY_CODE = Collections.unmodifiableMap(m);
    }

    /** 严格：码不存在抛异常 */
    public static NodeState ofCode(String code) {
        NodeState e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown NodeState code: " + code);
        return e;
    }

    /** 可空：码不存在返回 null */
    public static NodeState ofCodeOrNull(String code) {
        return BY_CODE.get(code);
    }

    /** 带默认：码不存在返回默认值 */
    public static NodeState ofCodeOrDefault(String code, NodeState def) {
        return BY_CODE.getOrDefault(code, def);
    }

}