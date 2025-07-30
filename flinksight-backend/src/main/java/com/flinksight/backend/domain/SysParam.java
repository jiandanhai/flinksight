package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统参数
 */
@Data
@Entity
@Table(name = "sys_param")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "系统参数表")
@Where(clause = "is_deleted=0")
public class SysParam implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "参数ID")
    private Long id;

    @Schema(description = "参数名称")
    private String name;

    @Schema(description = "参数编码")
    private String code;

    @Schema(description = "参数值")
    private String value;

    @Schema(description = "参数类型")
    private String type;

    @Schema(description = "备注")
    private String description;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
