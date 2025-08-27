package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 用户-API权限关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_api",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_api", columnNames = {"user_id", "api_id"}),
        indexes = {
                @Index(name = "idx_user_api_user", columnList = "user_id"),
                @Index(name = "idx_user_api_api", columnList = "api_id")
        }
)
@Schema(description = "用户-API权限关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"id"})
public class UserApi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "api_id", nullable = false)
    @Schema(description = "API ID")
    private Long apiId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
