package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobStatusEnum {
    RUNNING("running", "运行中"),
    FAILED("failed", "失败"),
    STOPPED("stopped", "已停止"),
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String desc;
}
