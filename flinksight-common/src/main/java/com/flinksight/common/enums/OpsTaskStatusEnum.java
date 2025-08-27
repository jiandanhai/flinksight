package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** 运维任务状态机（与表 ops_task.status 对齐） */
@Getter
@AllArgsConstructor
public enum OpsTaskStatusEnum {
  PENDING("0","待执行"),    // 待执行（新建）
  RUNNING("1","执行中"),    // 执行中（已开始）
  SUCCESS("2","成功结束"),    // 成功结束
  FAILED("3","执行失败"),     // 执行失败
  CANCELLED("4","人为取消");  // 人为取消

  private final String code;
  private final String desc;

  private static final Map<String, OpsTaskStatusEnum> BY_CODE;
  static {
    Map<String, OpsTaskStatusEnum> m = new HashMap<>();
    for (OpsTaskStatusEnum e : values()) m.put(e.code, e);
    BY_CODE = Collections.unmodifiableMap(m);
  }

  /** 严格：码不存在抛异常 */
  public static OpsTaskStatusEnum ofCode(String code) {
    OpsTaskStatusEnum e = BY_CODE.get(code);
    if (e == null) throw new IllegalArgumentException("Unknown OpsTaskStatusEnum code: " + code);
    return e;
  }

  /** 可空：码不存在返回 null */
  public static OpsTaskStatusEnum ofCodeOrNull(String code) {
    return BY_CODE.get(code);
  }

  /** 带默认：码不存在返回默认值 */
  public static OpsTaskStatusEnum ofCodeOrDefault(String code, OpsTaskStatusEnum def) {
    return BY_CODE.getOrDefault(code, def);
  }
}