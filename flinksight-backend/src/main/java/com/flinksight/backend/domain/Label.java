package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.Where;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * 标签表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "label")
@Schema(description = "标签表")
@Where(clause = "is_deleted=0")
public class Label implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键")
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    @Schema(description = "标签名称")
    private String name;

    @Column(name = "color", length = 20)
    @Schema(description = "颜色，可选")
    private String color;

    @Column(name = "type", length = 20)
    @Schema(description = "标签类型，可选")
    private String type;

    @Column(name = "tenant_id")
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "是否删除 0正常 1删除")
    private Integer isDeleted;
}
