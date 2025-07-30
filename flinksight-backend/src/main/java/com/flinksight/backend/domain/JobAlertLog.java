package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_alert_log")
@Schema(description = "作业报警日志表")
@Where(clause = "is_deleted=0")
public class JobAlertLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tenantId;
    private Long jobId;
    private String jobName;
    private String alertType;
    private String alertMsg;
    private LocalDateTime alertTime;
    private String status; // SENT/ACK
    private Integer isDeleted;
}
