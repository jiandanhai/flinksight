package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AlertLevelEnum {
    HIGH("high", "高"),
    MEDIUM("medium", "中"),
    LOW("low", "低");

    private final String code;
    private final String desc;
    private static final Map<String, AlertLevelEnum> BY_CODE;
    static {
        Map<String, AlertLevelEnum> m = new HashMap<>();
        for (AlertLevelEnum e : values()) m.put(e.code, e);
        BY_CODE = Collections.unmodifiableMap(m);
    }

    /** 严格：码不存在抛异常 */
    public static AlertLevelEnum ofCode(String code) {
        AlertLevelEnum e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown AlertLevelEnum code: " + code);
        return e;
    }

    /** 可空：码不存在返回 null */
    public static AlertLevelEnum ofCodeOrNull(String code) {
        return BY_CODE.get(code);
    }

    /** 带默认：码不存在返回默认值 */
    public static AlertLevelEnum ofCodeOrDefault(String code, AlertLevelEnum def) {
        return BY_CODE.getOrDefault(code, def);
    }
}
