package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源实体（支持JDBC/ES/CK等）
 */
@Data
@Entity
@Table(name = "data_source")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "数据源表")
@Where(clause = "is_deleted=0")
public class DataSource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "数据源ID")
    private Long id;

    @Schema(description = "数据源名称")
    private String name;

    @Schema(description = "数据源类型，如mysql、es、ck")
    private String type;

    @Schema(description = "连接信息(JSON/DSN)")
    @Column(columnDefinition = "text")
    private String connectInfo;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
