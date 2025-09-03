package com.flinksight.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Jsons {

    /** Spring 注入的全局 ObjectMapper（带你配置的模块） */
    private final ObjectMapper injected;

    /** 供静态方法使用的全局引用 */
    private static volatile ObjectMapper M;

    /** Spring 启动后把注入的 OM 绑定到静态引用 */
    @PostConstruct
    void init() { M = injected; }

    /** 可选：在测试/非 Spring 场景中手动初始化 */
    public static void init(ObjectMapper om) { M = om; }

    /** 取 Mapper（未初始化时给出明确错误） */
    private static ObjectMapper om() {
        if (M == null) {
            throw new IllegalStateException("Jsons not initialized: ObjectMapper not injected yet.");
        }
        return M;
    }

    // ===== 静态 API =====

    public static String to(Object obj) {
        try { return om().writeValueAsString(obj); }
        catch (JsonProcessingException e) { throw new RuntimeException(e); }
    }

    public static String pretty(Object obj) {
        try { return om().writerWithDefaultPrettyPrinter().writeValueAsString(obj); }
        catch (JsonProcessingException e) { throw new RuntimeException(e); }
    }

    public static <T> T from(String s, Class<T> t) {
        try { return om().readValue(s, t); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static <T> T from(String s, TypeReference<T> type) {
        try { return om().readValue(s, type); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static JsonNode parse(String s) {
        try { return om().readTree(s); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static JsonNode readTree(byte[] bytes) {
        try { return om().readTree(bytes); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    public static JsonNode readTree(java.io.InputStream in) {
        try { return om().readTree(in); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    // Jsons.java 内部新增
    public static JsonNode readTree(String text) {
        try { return om().readTree(text); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    // Jsons.java 里加
    public static ObjectNode object() {
        return om().createObjectNode();
    }

    //（可选）一起补个数组节点
    public static ArrayNode array() {
        return om().createArrayNode();
    }

    public static <T extends JsonNode> T toTree(Object src) {
        return om().valueToTree(src);
    }

    // === 通用转换 ===
    public static <T> T convert(Object src, Class<T> targetType) {
        return om().convertValue(src, targetType);
    }

    public static <T> T convert(Object src, com.fasterxml.jackson.core.type.TypeReference<T> targetType) {
        return om().convertValue(src, targetType);
    }

    // === 转 Map（最常用） ===
    public static java.util.Map<String, Object> toMap(Object src) {
        return om().convertValue(src, new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, Object>>() {});
    }

    // 可指定 key/value 类型
    public static <K, V> java.util.Map<K, V> toMap(Object src, Class<K> keyType, Class<V> valueType) {
        var tf = om().getTypeFactory();
        var mapType = tf.constructMapType(java.util.Map.class, keyType, valueType);
        return om().convertValue(src, mapType);
    }

    // === 其他常用集合（可选） ===
    public static <T> java.util.List<T> toList(Object src, Class<T> elemType) {
        var tf = om().getTypeFactory();
        var listType = tf.constructCollectionType(java.util.List.class, elemType);
        return om().convertValue(src, listType);
    }

    public static ObjectMapper mapper() { return om(); }
}
