package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 用户-分组关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_group",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_group", columnNames = {"user_id", "group_id"}),
        indexes = {
                @Index(name = "idx_user_group_user", columnList = "user_id"),
                @Index(name = "idx_user_group_group", columnList = "group_id")
        }
)
@Schema(description = "用户-分组关联表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class UserGroup implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "group_id", nullable = false)
    @Schema(description = "分组ID")
    private Long groupId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
