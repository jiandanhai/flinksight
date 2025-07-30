package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统配置/参数实体
 */
@Data
@Entity
@Table(name = "config")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统配置表")
@Where(clause = "is_deleted=0")
public class Config implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "参数ID")
    private Long id;

    @Schema(description = "参数编码")
    @Column(nullable = false, unique = true)
    private String code;

    @Schema(description = "参数值")
    @Column(columnDefinition = "text")
    private String value;

    @Schema(description = "参数说明")
    private String description;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
