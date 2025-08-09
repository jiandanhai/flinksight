package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 组织架构树节点DTO
 * 支持递归子节点展示
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "组织架构树节点DTO")
public class OrgNodeDTO implements Serializable {

    @Schema(description = "节点ID")
    private Long id;

    @Schema(description = "父节点ID")
    private Long parentId;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "节点类型（公司/部门/组等）")
    private String type;

    @Schema(description = "排序序号")
    private Integer sortOrder;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "是否删除 0=正常 1=删除")
    private Integer isDeleted;

    @Schema(description = "子节点列表（树结构）")
    private List<OrgNodeDTO> children;
}
