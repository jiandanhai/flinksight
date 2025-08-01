package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知消息表
 * 支持平台内系统消息、告警、待办、推送等
 */
@Getter
@Setter
@Entity
@Table(
        name = "notification",
        indexes = {
                @Index(name = "idx_notify_user_tenant_read", columnList = "user_id, tenant_id, is_read"),
                @Index(name = "idx_notify_time", columnList = "create_time")
        }
)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通知消息表")
@SQLRestriction("is_deleted=0") // 替代 Hibernate 6.3 的 @Where
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "通知ID")
    private Long id;

    @Column(name = "user_id", nullable = false)
    @Schema(description = "接收用户ID")
    private Long userId;

    @Column(name = "tenant_id", nullable = false)
    @Schema(description = "租户ID")
    private Long tenantId;

    @Column(name = "title", nullable = false, length = 128)
    @Schema(description = "通知标题")
    private String title;

    @Column(name = "content", columnDefinition = "text")
    @Schema(description = "通知内容")
    private String content;

    @Column(name = "type", length = 32)
    @Schema(description = "通知类型")
    private String type;

    @Column(name = "is_read", nullable = false)
    @Schema(description = "已读标志 0=未读 1=已读")
    private Integer isRead = 0;

    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @Column(name = "create_time", nullable = false)
    @Schema(description = "发送时间")
    private LocalDateTime createTime;
}
