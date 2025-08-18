package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum AlertStatusEnum {
    UNRESOLVED(0, "未处理"),
    PROCESSING(1, "处理中"),
    CLOSED(2, "已关闭");

    private final Integer code;
    private final String desc;

    private static final Map<Integer, AlertStatusEnum> BY_CODE;
    static {
        Map<Integer, AlertStatusEnum> m = new HashMap<>();
        for (AlertStatusEnum e : values()) m.put(e.code, e);
        BY_CODE = Collections.unmodifiableMap(m);
    }

    public static AlertStatusEnum ofCode(Integer code) {
        AlertStatusEnum e = BY_CODE.get(code);
        if (e == null) throw new IllegalArgumentException("Unknown AlertStatusEnum code: " + code);
        return e;
    }
    public static AlertStatusEnum ofCodeOrNull(Integer code) { return BY_CODE.get(code); }
    public static AlertStatusEnum ofCodeOrDefault(Integer code, AlertStatusEnum def) {
        return BY_CODE.getOrDefault(code, def);
    }
}
