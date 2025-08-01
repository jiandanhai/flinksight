package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 用户-岗位关联表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_post",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_post", columnNames = {"user_id", "post_id"}),
        indexes = {
                @Index(name = "idx_user_post_user", columnList = "user_id"),
                @Index(name = "idx_user_post_post", columnList = "post_id")
        }
)
@Schema(description = "用户-岗位关联表")
@SQLRestriction("is_deleted=0") // Hibernate 6.3 推荐软删除方式
public class UserPost implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "post_id", nullable = false)
    @Schema(description = "岗位ID")
    private Long postId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
