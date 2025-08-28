package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AlertLevel {
    FATAL("0", "致命"),
    HIGH("1", "高"),
    MEDIUM("2", "中"),
    LOW("3", "低"),
    UNKNOWN("4", "未知");



    private final String code;
    private final String desc;
    private static final Map<String, AlertLevel> BY_CODE;
    static {
        Map<String, AlertLevel> m = new HashMap<>();
        for (AlertLevel e : values()) m.put(e.code, e);
        BY_CODE = Collections.unmodifiableMap(m);
    }

    /** 严格：码不存在抛异常 */
    public static AlertLevel ofCode(String code) {
        AlertLevel e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown AlertLevelEnum code: " + code);
        return e;
    }

    /** 可空：码不存在返回 null */
    public static AlertLevel ofCodeOrNull(String code) {
        return BY_CODE.get(code);
    }

    /** 带默认：码不存在返回默认值 */
    public static AlertLevel ofCodeOrDefault(String code, AlertLevel def) {
        return BY_CODE.getOrDefault(code, def);
    }
}
