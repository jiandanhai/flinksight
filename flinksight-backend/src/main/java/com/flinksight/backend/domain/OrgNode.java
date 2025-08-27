package com.flinksight.backend.domain;

import com.flinksight.common.service.DefaultSort;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 组织架构树节点实体
 * 支持树形结构
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "org_node")
@DefaultSort(fields = {"createdAt", "id"})
public class OrgNode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentId;

    @Column(nullable = false, length = 128)
    private String name;

    @Column(length = 32)
    private String type;

    private Integer sortOrder;

    @Column(nullable = false)
    private Long tenantId;

    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Integer isDeleted;
}
