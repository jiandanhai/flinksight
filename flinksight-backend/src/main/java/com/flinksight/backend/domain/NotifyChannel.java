package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知渠道实体
 * 映射表 notify_channel
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notify_channel")
@DefaultSort(fields = {"createdAt", "id"})
public class NotifyChannel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 32)
    private String type;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String config;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(nullable = false)
    private Integer enabled;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer isDeleted;
}
