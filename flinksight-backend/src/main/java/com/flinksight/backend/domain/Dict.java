package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 数据字典表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dict")
@Schema(description = "数据字典表")
@Where(clause = "is_deleted=0")
public class Dict implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "dict_type", nullable = false, length = 50)
    @Schema(description = "字典类型")
    private String dictType;

    @Column(name = "dict_key", nullable = false, length = 50)
    @Schema(description = "字典项KEY")
    private String dictKey;

    @Column(name = "dict_value", nullable = false, length = 100)
    @Schema(description = "字典项VALUE")
    private String dictValue;

    @Column(name = "sort", nullable = false)
    @Schema(description = "排序")
    private Integer sort;

    @Column(name = "description", length = 255)
    @Schema(description = "描述")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
