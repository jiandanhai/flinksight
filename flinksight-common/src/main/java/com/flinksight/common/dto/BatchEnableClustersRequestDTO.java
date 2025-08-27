package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

@Data
@Schema(description = "批量启用/禁用集群请求")
public class BatchEnableClustersRequestDTO {
    @Schema(description = "集群ID集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<Long> ids;

    @Schema(description = "是否启用：true=启用；false=禁用", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean enable;
}