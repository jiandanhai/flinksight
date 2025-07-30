package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息/个人中心扩展表
 */
@Data
@Entity
@Table(name = "profile")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户个人信息表")
@Where(clause = "is_deleted=0")
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "性别")
    private Integer gender;

    @Schema(description = "部门")
    private String department;

    @Schema(description = "岗位")
    private String position;

    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "手机")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
