package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum JobStatus {
    CREATED(0, "新建"),
    RUNNING(1, "运行中"),
    FAILED(2, "失败"),
    STOPPED(3, "已停止"),
    RESTARTING(4, "重试中"),
    UNKNOWN(5, "未知");

    private final Integer code;
    private final String label;
    private final Set<String> aliases; // 预留别名/多语言（可为空）

    JobStatus(int code, String label, String... aliases) {
        this.code = code;
        this.label = label;
        this.aliases = aliases == null ? Set.of() :
                Arrays.stream(aliases).filter(Objects::nonNull)
                        .map(JobStatus::normalize)
                        .collect(Collectors.toUnmodifiableSet());
    }

    public int getCode()   { return code; }
    public String getLabel(){ return label; }

    /* ------------ 索引表：高效反查 ------------ */
    private static final Map<Integer, JobStatus> BY_CODE =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(JobStatus::getCode, e -> e));

    private static final Map<String, JobStatus> BY_LABEL =
            Arrays.stream(values()).collect(Collectors.toUnmodifiableMap(
                    e -> normalize(e.getLabel()),
                    e -> e
            ));

    // label + aliases 统一索引（允许“运行中”、“running”等映射到同一枚举）
    private static final Map<String, JobStatus> BY_TEXT;
    static {
        Map<String, JobStatus> map = new HashMap<>(BY_LABEL);
        for (JobStatus e : values()) {
            for (String a : e.aliases) {
                map.putIfAbsent(a, e);
            }
        }
        BY_TEXT = Collections.unmodifiableMap(map);
    }

    private static String normalize(String s) {
        return s == null ? null : s.trim().toLowerCase(Locale.ROOT);
    }

    /* ------------ 反查：code ↔ label ------------ */

    /** 由 code 取枚举；未命中返回 UNKNOWN（容错） */
    public static JobStatus ofCode(Integer code) {
        if (code == null) return UNKNOWN;
        return BY_CODE.getOrDefault(code, UNKNOWN);
    }

    /** 由 label/别名 取枚举；未命中返回 UNKNOWN（容错） */
    public static JobStatus ofLabel(String label) {
        if (label == null) return UNKNOWN;
        return BY_TEXT.getOrDefault(normalize(label), UNKNOWN);
    }

    /** Optional 版本：未命中返回 empty（需要区分未命中时可用） */
    public static Optional<JobStatus> findByCode(Integer code) {
        return Optional.ofNullable(BY_CODE.get(code));
    }

    public static Optional<JobStatus> findByLabel(String label) {
        return Optional.ofNullable(BY_TEXT.get(normalize(label)));
    }

    /** label -> code（未命中给 UNKNOWN 的 code） */
    public static int codeOfLabel(String label) {
        return ofLabel(label).getCode();
    }

    /** code -> label（未命中给 UNKNOWN 的 label） */
    public static String labelOfCode(Integer code) {
        return ofCode(code).getLabel();
    }

    /* ------------ 通用解析（适配 DB 投影 Object） ------------ */

    /**
     * 从数据库聚合/投影返回的 key 解析为枚举：
     * - Number：按数值解析
     * - String：优先解析为 int，否则按 label/别名解析
     * - 其它：UNKNOWN
     */
    public static JobStatus fromDbKey(Object key) {
        if (key == null) return UNKNOWN;
        if (key instanceof Number n) {
            return ofCode(n.intValue());
        }
        if (key instanceof String s) {
            try {
                return ofCode(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignore) {
                return ofLabel(s);
            }
        }
        return UNKNOWN;
    }

    /* ------------ 便捷方法 ------------ */

    /** 是否属于给定集合之一 */
    public boolean in(JobStatus... set) {
        if (set == null || set.length == 0) return false;
        for (JobStatus e : set) {
            if (this == e) return true;
        }
        return false;
    }

    /** 枚举名 + (code:label) 便于日志 */
    @Override public String toString() {
        return name() + "(" + code + ":" + label + ")";
    }
}
