package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JobStatusEnum {
    CREATED(0, "新建"),
    RUNNING(1, "运行中"),
    FAILED(2, "失败"),
    STOPPED(3, "已停止"),
    RESTARTING(4, "重试中"),
    UNKNOWN(5, "未知");

    private final Integer code;
    private final String label;
}
