package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据字典DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据字典DTO")
public class DictDTO  implements Serializable {
    private Long id;
    private String dictType;
    private String dictKey;
    private String dictValue;
    private Integer sort;
    private String description;
    private Integer isDeleted;
}
