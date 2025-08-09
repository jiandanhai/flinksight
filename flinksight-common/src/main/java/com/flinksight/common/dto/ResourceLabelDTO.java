package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "资源-标签关联DTO")
public class ResourceLabelDTO  implements Serializable {
    private Long id;
    private Long resourceId;
    private Long labelId;
    private Integer isDeleted;
}
