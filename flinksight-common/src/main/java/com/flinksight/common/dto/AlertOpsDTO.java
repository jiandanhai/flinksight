package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "告警操作视图（可执行动作 + 最近历史）")
public class AlertOpsDTO {
    private Long alertId;
    private List<String> allowedActions; // ACKNOWLEDGE/ASSIGN/CLOSE
    private List<AlertHistoryDTO> lastHistories;
}