package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "节点健康查询返回")
public class NodeHealthResponseDTO {
    private String latestStatus;
    private List<NodeHealthPointDTO> items;
}