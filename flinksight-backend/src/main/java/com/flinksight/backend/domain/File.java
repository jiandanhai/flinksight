package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件/附件实体
 */
@Getter
@Setter
@Entity
@Table(
        name = "`file`", // ⚡ 使用反引号避免 MySQL 保留字冲突
        indexes = {
                @Index(name = "idx_tenant", columnList = "tenant_id"),
                @Index(name = "idx_user", columnList = "user_id"),
                @Index(name = "idx_type", columnList = "type")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文件表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
public class File implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "文件ID")
    private Long id;

    @Column(nullable = false, length = 255)
    @Schema(description = "文件名")
    private String name;

    @Column(nullable = false, length = 512)
    @Schema(description = "存储路径/URL")
    private String url;

    @Column(nullable = false, length = 64)
    @Schema(description = "文件类型")
    private String type;

    @Column(nullable = false)
    @Schema(description = "文件大小（字节）")
    private Long size;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "上传用户ID")
    private Long userId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Column(name = "created_at", updatable = false)
    @Schema(description = "上传时间")
    private LocalDateTime createdAt;
}
