package com.flinksight.common.dto;

import lombok.*;
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
    private LocalDateTime ts;
    private Integer isDeleted;
}
