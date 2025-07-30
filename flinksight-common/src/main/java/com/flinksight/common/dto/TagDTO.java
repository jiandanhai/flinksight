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
@Schema(description = "标签DTO")
public class TagDTO {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "标签名")
    private String name;

    @Schema(description = "标签类型")
    private String type;

    @Schema(description = "标签颜色")
    private String color;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
