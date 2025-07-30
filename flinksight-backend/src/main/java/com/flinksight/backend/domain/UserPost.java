package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 用户-岗位关联表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_post", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"}))
@Schema(description = "用户-岗位关联表")
@Where(clause = "is_deleted=0")
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
    private Integer isDeleted;
}
