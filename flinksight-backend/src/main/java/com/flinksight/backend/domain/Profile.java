package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户个人信息表
 * 用户信息/个人中心扩展表
 */
@Getter
@Setter
@Entity
@Table(
        name = "profile",
        uniqueConstraints = @UniqueConstraint(name = "uk_profile_user_id", columnNames = {"user_id"})
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户个人信息表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "real_name", length = 64)
    @Schema(description = "真实姓名")
    private String realName;

    @Column(name = "avatar_url", length = 256)
    @Schema(description = "头像URL")
    private String avatarUrl;

    @Column(name = "gender")
    @Schema(description = "性别")
    private Integer gender;

    @Column(name = "department", length = 64)
    @Schema(description = "部门")
    private String department;

    @Column(name = "position", length = 64)
    @Schema(description = "岗位")
    private String position;

    @Column(name = "signature", length = 128)
    @Schema(description = "个性签名")
    private String signature;

    @Column(name = "phone", length = 32)
    @Schema(description = "手机")
    private String phone;

    @Column(name = "email", length = 64)
    @Schema(description = "邮箱")
    private String email;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
