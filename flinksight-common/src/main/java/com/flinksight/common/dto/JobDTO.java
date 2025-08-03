package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long clusterId;
    private String name;
    private String type; // streaming/batch
    private String status;
    private Long ownerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
