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
public class JobLogDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long jobId;
    private String level;
    private String content;
    private LocalDateTime logTime;
    private Integer isDeleted;
}
