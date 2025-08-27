package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API访问日志
 */
@Getter
@Setter
@Entity
@Table(
        name = "api_access_log",
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_user", columnList = "user_id"),
                @Index(name = "idx_time", columnList = "access_time")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API访问日志表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"accessTime", "id"})
public class ApiAccessLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Column(length = 512)
    @Schema(description = "请求URL")
    private String url;

    @Column(name = "http_method", length = 16)
    @Schema(description = "HTTP方法")
    private String httpMethod;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "请求参数")
    private String params;

    @Column
    @Schema(description = "响应码")
    private Integer status;

    @Column(name = "user_id")
    @Schema(description = "访问用户ID")
    private Long userId;

    @Column(name = "tenant_id")
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(length = 64)
    @Schema(description = "IP地址")
    private String ip;

    @Column(name = "access_time")
    @Schema(description = "访问时间")
    private LocalDateTime accessTime;

    @Column
    @Schema(description = "耗时ms")
    private Long duration;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
