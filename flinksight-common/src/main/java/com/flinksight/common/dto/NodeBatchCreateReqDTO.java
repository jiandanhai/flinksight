package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "批量创建节点")
public class NodeBatchCreateReqDTO {
    @Valid
    @NotEmpty
    private List<NodeDTO> items;
}