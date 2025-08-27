package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(description = "节点健康点位")
public class NodeHealthPointDTO {
    private LocalDateTime time;
    private String status;
    private String message;
}