package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API访问日志
 */
@Data
@Entity
@Table(name = "api_access_log")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API访问日志表")
@Where(clause = "is_deleted=0")
public class ApiAccessLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "请求URL")
    private String url;

    @Schema(description = "HTTP方法")
    private String httpMethod;

    @Schema(description = "请求参数")
    @Column(columnDefinition = "text")
    private String params;

    @Schema(description = "响应码")
    private Integer status;

    @Schema(description = "访问用户ID")
    private Long userId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "访问时间")
    private LocalDateTime accessTime;

    @Schema(description = "耗时ms")
    private Long duration;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
