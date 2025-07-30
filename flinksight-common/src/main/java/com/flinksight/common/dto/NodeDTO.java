package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 节点DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "节点DTO")
public class NodeDTO {
    private Long id;
    private String name;
    private String type;
    private String ip;
    private Long clusterId;
    private Integer status;
    private Integer isDeleted;
    private LocalDateTime createTime;
}
