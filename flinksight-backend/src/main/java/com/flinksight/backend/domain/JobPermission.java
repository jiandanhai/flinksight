package com.flinksight.backend.domain;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 作业权限表（作业-用户-权限三元组）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_permission")
public class JobPermission implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "user_id", nullable = false, length = 64)
    private String userId;

    @Column(name = "permission_id", nullable = false)
    private Long permissionId;
}
