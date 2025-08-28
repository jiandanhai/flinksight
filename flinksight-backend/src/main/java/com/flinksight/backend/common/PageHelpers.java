package com.flinksight.backend.common;

import com.flinksight.common.model.PageResult;
import com.flinksight.common.service.DefaultSort;
import jakarta.persistence.Column;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

/** 统一分页工具（项目标准做法） */
public final class PageHelpers {
  private PageHelpers() {}

  /** 前端 1-based → 0-based（守护） */
  public static int toZeroBased(int pageOneBased) {
    return Math.max(0, pageOneBased - 1);
  }

  /** 0-based → 1-based（守护） */
  public static int toOneBased(int pageZeroBased) {
    return pageZeroBased + 1;
  }

  /** 生成 PageRequest（按 1-based page 入参） */
  public static PageRequest pageRequest(int pageOneBased, int size) {
    int p = toZeroBased(pageOneBased);
    int s = size > 0 ? size : 20;
    return PageRequest.of(p, s);
  }

  public static PageRequest pageRequest(int pageOneBased, int size, Sort sort) {
    int p = toZeroBased(pageOneBased);
    int s = size > 0 ? size : 20;
    return (sort == null || sort.isUnsorted())
            ? PageRequest.of(p, s)
            : PageRequest.of(p, s, sort);
  }

  /**
   * 当 sort==null 时，按实体上的 @DefaultSort 生成；
   * 同时对传入 sort 做字段映射（snake→camel、@Column 映射、大小写不敏感匹配）。
   * ⚠️ 不做 ignoreCase / nullsLast，以避免 lower(timestamp/number) 之类的问题。
   */
  public static PageRequest pageRequest(int pageOneBased, int size, Sort incomingSort, Class<?> entityClass) {
    int p = toZeroBased(pageOneBased);
    int s = size > 0 ? size : 20;

    Sort normalized = normalizeSort(incomingSort, entityClass);
    if (normalized == null || normalized.isUnsorted()) {
      normalized = defaultSortOf(entityClass); // 仅按 @DefaultSort；没有则 unsorted
    }

    return (normalized == null || normalized.isUnsorted())
            ? PageRequest.of(p, s)
            : PageRequest.of(p, s, normalized);
  }

  /** Page<E> → PageResult<R>，返回时把 page 改成 1-based（前端友好） */
  public static <E, R> PageResult<R> toPageResult(Page<E> page, Function<E, R> mapper, boolean returnOneBased) {
    PageResult<R> pr = PageResult.from(page.map(mapper));
    if (returnOneBased) {
      pr.setPage(toOneBased(pr.getPage())); // PageResult.page 默认是 0-based
    }
    return pr;
  }

  // -------------------- 内部：基于实体元数据的通用映射（无别名、无硬编码） --------------------

  /** 把 snake_case 转为 camelCase；无下划线则原样返回 */
  private static String snakeToCamel(String name) {
    if (name == null || name.isBlank() || !name.contains("_")) return name;
    StringBuilder sb = new StringBuilder(name.length());
    boolean up = false;
    for (char c : name.toCharArray()) {
      if (c == '_') { up = true; continue; }
      sb.append(up ? Character.toUpperCase(c) : c);
      up = false;
    }
    return sb.toString();
  }

  /** 简单的实体元数据容器（仅从反射与注解中收集） */
  private record EntityMeta(Set<String> properties,
                            Map<String, String> columnToProperty) {
    private static final EntityMeta EMPTY =
            new EntityMeta(Collections.emptySet(), Collections.emptyMap());
  }

  /** 收集实体字段元数据：属性名集合、@Column 列名 → 属性名 的映射（忽略大小写） */
  private static EntityMeta collectEntityMeta(Class<?> entityClass) {
    if (entityClass == null) return EntityMeta.EMPTY;

    Set<String> props = new HashSet<>();
    Map<String, String> columnToProp = new HashMap<>();

    for (Class<?> c = entityClass; c != null && c != Object.class; c = c.getSuperclass()) {
      for (Field f : c.getDeclaredFields()) {
        String prop = f.getName();
        props.add(prop);

        Column col = f.getAnnotation(Column.class);
        if (col != null) {
          String colName = col.name();
          if (colName != null && !colName.isBlank()) {
            columnToProp.put(colName.toLowerCase(Locale.ROOT), prop);
          }
        }
      }
    }
    return new EntityMeta(props, columnToProp);
  }

  /** 将外部字段名映射为实体属性名（大小写不敏感、支持 @Column 与 snake→camel；无匹配返回 null） */
  private static String mapToProperty(EntityMeta meta, String name) {
    if (meta == null || name == null || name.isBlank()) return null;

    // 1) 精确命中属性名
    if (meta.properties.contains(name)) return name;

    // 2) 忽略大小写属性名
    for (String p : meta.properties) {
      if (p.equalsIgnoreCase(name)) return p;
    }

    // 3) 列名（@Column），忽略大小写
    String byColumn = meta.columnToProperty.get(name.toLowerCase(Locale.ROOT));
    if (byColumn != null) return byColumn;

    // 4) snake_case → camelCase 后再试
    String camel = snakeToCamel(name);
    if (!Objects.equals(camel, name)) {
      if (meta.properties.contains(camel)) return camel;
      for (String p : meta.properties) {
        if (p.equalsIgnoreCase(camel)) return p;
      }
    }

    // 5) 进一步容错：去下划线后忽略大小写比较
    String flat = name.replace("_", "").toLowerCase(Locale.ROOT);
    for (String p : meta.properties) {
      String pf = p.replace("_", "").toLowerCase(Locale.ROOT);
      if (pf.equals(flat)) return p;
    }

    return null;
  }

  /** 规范化传入的 Sort：仅保留能映射到实体属性的字段；其余丢弃（不做 ignoreCase/nullsLast） */
  private static Sort normalizeSort(Sort sort, Class<?> entityClass) {
    if (sort == null || sort.isUnsorted() || entityClass == null) return sort;

    EntityMeta meta = collectEntityMeta(entityClass);
    List<Sort.Order> normalized = new ArrayList<>();

    for (Sort.Order o : sort) {
      String raw = o.getProperty();
      String prop = mapToProperty(meta, raw);
      if (prop != null) {
        normalized.add(new Sort.Order(o.getDirection(), prop));
      }
    }

    return normalized.isEmpty() ? Sort.unsorted() : Sort.by(normalized);
  }

  /** 读取实体上的 @DefaultSort 注解；无注解或无有效字段则返回 unsorted（不做 ignoreCase/nullsLast） */
  private static Sort defaultSortOf(Class<?> entityClass) {
    if (entityClass == null) return Sort.unsorted();
    DefaultSort ds = entityClass.getAnnotation(DefaultSort.class);
    if (ds == null || ds.fields().length == 0) return Sort.unsorted();

    List<Sort.Order> orders = new ArrayList<>();
    EntityMeta meta = collectEntityMeta(entityClass);
    for (String f : ds.fields()) {
      if (f == null || f.isBlank()) continue;
      String prop = mapToProperty(meta, f.trim());
      if (prop != null) {
        orders.add(new Sort.Order(ds.direction(), prop));
      }
    }
    return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
  }

  // -------------------- 可选：从字符串构建 Sort（最终仍会在 pageRequest(..., entityClass) 中映射） --------------------

  /** Controller 用：?sort=xxx&order=ASC|DESC → Sort（不做 ignoreCase/nullsLast） */
  public static Sort buildSort(String sortField, String order) {
    if (sortField == null || sortField.isBlank()) return Sort.unsorted();
    Sort.Direction dir = "ASC".equalsIgnoreCase(order) ? Sort.Direction.ASC : Sort.Direction.DESC;
    return Sort.by(new Sort.Order(dir, sortField.trim()));
  }
}
