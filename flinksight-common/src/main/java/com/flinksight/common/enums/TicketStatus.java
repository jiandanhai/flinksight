package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TicketStatus {
    UNRESOLVED(0, "未处理"),
    PROCESSING(1, "处理中"),
    CLOSED(2, "已关闭");

    private final int code;
    private final String desc;
}
