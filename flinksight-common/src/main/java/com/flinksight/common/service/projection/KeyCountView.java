package com.flinksight.common.service.projection;

// 可放在 common 模块：com.flinksight.common.projection
public interface KeyCountView {
    Object getKey();   // 根据表字段，可能是 Integer/String/Enum，先用 Object 接
    Long getCnt();
}