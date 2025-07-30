package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件/附件实体
 */
@Data
@Entity
@Table(name = "file")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "文件表")
@Where(clause = "is_deleted=0")
public class File implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "文件ID")
    private Long id;

    @Schema(description = "文件名")
    private String name;

    @Schema(description = "存储路径/URL")
    private String url;

    @Schema(description = "文件类型")
    private String type;

    @Schema(description = "文件大小（字节）")
    private Long size;

    @Schema(description = "上传用户ID")
    private Long userId;

    @Schema(description = "所属租户ID")
    private Long tenantId;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "上传时间")
    private LocalDateTime createTime;
}
