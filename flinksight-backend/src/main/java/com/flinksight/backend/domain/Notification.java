package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知消息实体
 */
@Data
@Entity
@Table(name = "notification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知消息表")
@Where(clause = "is_deleted=0")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "通知ID")
    private Long id;

    @Schema(description = "接收用户ID")
    private Long userId;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    @Column(columnDefinition = "text")
    private String content;

    @Schema(description = "通知类型")
    private String type;

    @Schema(description = "已读标志")
    private Integer isRead = 0;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "发送时间")
    private LocalDateTime createTime;
}
