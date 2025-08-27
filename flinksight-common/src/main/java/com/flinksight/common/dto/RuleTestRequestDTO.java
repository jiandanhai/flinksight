package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/** 规则测试入参：可给 metricKey/op/threshold，也可给 type/message 让服务端解析 */
@Data
@Schema(description = "规则测试匹配入参")
public class RuleTestRequestDTO {
    private Long clusterId;

    /** 二选一：给定告警类型（如 CPUHigh/MEMHigh/JobFailed...），服务端映射 metricKey */
    private String type;

    /** 二选一：直接给定指标 key（如 cpu/mem/job/disk） */
    private String metricKey;

    /** 二选一：直接给定比较符（>, >=, <, <=, =） */
    private String compareOp;

    /** 二选一：阈值，支持 0~1 范围（若原始表达式是百分比，前端换算或由 message 解析） */
    private Double threshold;

    /** 可选：若未给 compareOp/threshold，可从 message 中解析（如 "CPU usage > 85%"） */
    private String message;
}