package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
        name = "profile",
        uniqueConstraints = @UniqueConstraint(name = "uk_tenant_user", columnNames = {"tenant_id","user_id"}),
        indexes = {
                @Index(name = "idx_profile_tenant", columnList = "tenant_id"),
                @Index(name = "idx_profile_user", columnList = "user_id") // ✅ 新增：常用查询键
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户个人信息表（按租户维度）")
@SQLRestriction("is_deleted=0")
@SQLDelete(sql = "UPDATE profile SET is_deleted=1 WHERE id=?")
@DefaultSort(fields = {"createTime", "id"})
public class Profile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "用户ID")
    private Long userId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

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

    @Column(name = "email", length = 160) // ▶️ 建议放宽到 160
    @Schema(description = "邮箱")
    private String email;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    // 方便读取（不改你的 userId 字段），保持只读关联
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
