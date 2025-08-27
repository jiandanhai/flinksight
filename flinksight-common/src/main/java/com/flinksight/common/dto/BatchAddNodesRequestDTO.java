package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "批量新增节点请求")
public class BatchAddNodesRequestDTO {
    @Schema(description = "所属集群ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long clusterId;

    @Schema(description = "节点列表", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<NodeDTO> nodes;
}