package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority; // 需要导入

import java.io.Serializable;
import java.util.List;

/**
 * 角色实体
 * Role Entity
 */
@Data
@Entity
@Table(name = "role")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "角色表")
@Where(clause = "is_deleted=0")
public class Role implements GrantedAuthority, Serializable { // <----- 这里实现接口
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "角色ID")
    private Long id;

    @Column(nullable = false, length = 50)
    @Schema(description = "角色名称")
    private String name;

    @Column(nullable = false, length = 50)
    @Schema(description = "角色编码")
    private String code;

    @Column(length = 100)
    @Schema(description = "角色描述")
    private String desc;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Schema(description = "用户集合")
    private List<User> users;

    /**
     * 返回权限字符串（角色名/编码均可）
     * Spring Security将自动识别
     */
    @Override
    public String getAuthority() {
        // 一般用code，如果需要和Security角色前缀保持一致可以加 "ROLE_" 前缀
        return code;
    }

    @Column(name = "is_deleted", nullable = false, columnDefinition = "tinyint default 0")
    @Schema(description = "软删除")
    private Integer isDeleted;
}
