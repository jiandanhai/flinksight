package com.flinksight.backend.domain;


import com.flinksight.common.service.DefaultSort;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.io.Serializable;
import java.time.LocalDateTime;


@Getter @Setter
@Entity
@Table(
        name = "notification",
        indexes = {
                @Index(name = "idx_notify_user_tenant_read", columnList = "user_id, tenant_id, read_status"),
                @Index(name = "idx_notify_time", columnList = "create_time")
        }
)
@Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "通知消息表")
@SQLRestriction("is_deleted=0")
@DefaultSort(fields = {"createTime", "id"})
public class Notification implements Serializable {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "通知ID")
    private Long id;


    @Column(name = "user_id", nullable = false)
    private Long userId;


    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;


    @Column(name = "title", nullable = false, length = 128)
    private String title;


    @Column(name = "content", columnDefinition = "text")
    private String content;


    // 明确命名：类别
    @Column(name = "category", length = 32)
    @Schema(description = "通知类别（如 ALERT/SYSTEM/MARKETING/INFO 等）")
    private String category;


    // 0=未读 1=已读（数据库上存 0/1）
    @Column(name = "read_status", nullable = false)
    @Schema(description = "已读标志：0=未读 1=已读")
    private Integer readStatus = 0;


    @Column(name = "is_deleted", nullable = false)
    private Integer isDeleted = 0;


    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
}