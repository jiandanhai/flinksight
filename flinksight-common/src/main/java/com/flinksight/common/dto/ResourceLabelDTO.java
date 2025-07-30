package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源-标签关联DTO")
public class ResourceLabelDTO {
    private Long id;
    private Long resourceId;
    private Long labelId;
    private Integer isDeleted;
}
