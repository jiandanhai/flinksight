package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_alert_rule")
@Schema(description = "作业报警规则表")
@Where(clause = "is_deleted=0")
public class JobAlertRule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tenantId;
    private String ruleName;
    private String jobType;
    private String alertType; // EXCEPTION/DELAY/LAG
    private String conditionJson; // 阈值/表达式JSON
    private String target; // 通知目标
    private Integer isDeleted;
}
