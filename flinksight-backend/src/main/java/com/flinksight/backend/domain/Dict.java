package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;

/**
 * 数据字典表
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "dict",
        uniqueConstraints = @UniqueConstraint(name = "uk_dict_type_key", columnNames = {"dict_type", "dict_key"}),
        indexes = {
                @Index(name = "idx_dict_type", columnList = "dict_type")
        }
)
@Schema(description = "数据字典表")
@SQLRestriction("is_deleted=0") // ⚡ 替代 Hibernate 6.3 的 @Where
@DefaultSort(fields = {"dictKey", "id"})
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
    private Integer sort = 0; // 默认0，避免 null

    @Column(name = "description", length = 255)
    @Schema(description = "描述")
    private String description;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted = 0;
}
