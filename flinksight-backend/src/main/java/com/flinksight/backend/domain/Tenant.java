package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 租户实体
 * Tenant Entity
 */
@Data
@Entity
@Table(name = "tenant")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "租户表")
@Where(clause = "is_deleted=0")
public class Tenant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "租户ID")
    private Long id;

    @Column(nullable = false, length = 64)
    @Schema(description = "租户名称")
    private String name;

    @Column(nullable = false, unique = true, length = 64)
    @Schema(description = "租户编码")
    private String code;

    @Column(length = 100)
    @Schema(description = "联系人")
    private String contact;

    @Column(nullable = false, columnDefinition = "tinyint default 1")
    @Schema(description = "状态")
    private Integer status;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;
}
