package com.flinksight.common.service.projection;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class KeyCountMapper {

    private KeyCountMapper() {}

    public static <T> List<KeyCount<T>> map(List<KeyCountView> rows,
                                            Function<Object, T> keyMapper) {
        return rows.stream()
                .map(r -> new KeyCount<>(keyMapper.apply(r.getKey()), r.getCnt()))
                .toList();
    }

    public static Integer asInt(Object o) {
        return o == null ? null : ((Number) o).intValue();
    }

    public static String asString(Object o) {
        return o == null ? null : Objects.toString(o, null);
    }

    /** 结果 DTO（发给前端更友好） */
    public record KeyCount<T>(T key, Long cnt) {}
}