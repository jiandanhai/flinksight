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
public class TicketDTO implements Serializable {
    private Long id;
    private Long tenantId;
    private Long alertId;
    private Long handlerId;
    private Integer status;
    private String note;
    private Integer isDeleted;
    private LocalDateTime updateTime;
}
