package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;

/**
 * 系统设置（如品牌名、登录背景等）
 */
@Data
@Entity
@Table(name = "system_settings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "全局系统设置表")
@Where(clause = "is_deleted=0")
public class SystemSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "设置项编码")
    @Column(nullable = false, unique = true)
    private String code;

    @Schema(description = "设置项值")
    private String value;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
