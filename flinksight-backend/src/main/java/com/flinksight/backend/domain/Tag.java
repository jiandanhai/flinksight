package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 标签实体
 */
@Data
@Entity
@Table(name = "tag")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "标签表")
@Where(clause = "is_deleted=0")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "标签ID")
    private Long id;

    @Schema(description = "标签名")
    private String name;

    @Schema(description = "标签颜色")
    private String color;

    @Schema(description = "标签类型")
    private String type;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
