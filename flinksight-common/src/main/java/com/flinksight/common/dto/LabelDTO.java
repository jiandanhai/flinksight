package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 标签DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签DTO")
public class LabelDTO  implements Serializable {
    private Long id;
    private String name;
    private String color;
    private String type;
    private Long tenantId;
    private Integer isDeleted;
}
