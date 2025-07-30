package com.flinksight.common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long jobId;
    private String level;
    private String type;
    private String message;
    private Integer status;
    private Long handlerId;
    private Integer isDeleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
