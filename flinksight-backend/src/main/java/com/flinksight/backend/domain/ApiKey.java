package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * API密钥/Token表
 */
@Data
@Entity
@Table(name = "api_key")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "API密钥表")
@Where(clause = "is_deleted=0")
public class ApiKey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "密钥ID")
    private Long id;

    @Schema(description = "密钥名称")
    private String name;

    @Schema(description = "API密钥内容")
    private String apiKey;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "关联用户ID")
    private Long userId;

    @Schema(description = "密钥状态 0正常 1禁用")
    private Integer status;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;
}
