  package com.flinksight.backend.domain;

  import com.flinksight.common.service.DefaultSort;
  import io.swagger.v3.oas.annotations.media.Schema;
  import jakarta.persistence.*;
  import lombok.*;

  import java.io.Serializable;
  import java.time.OffsetDateTime;

  /**
   * 菜单实体（仅在仓储与服务内部使用；对外不暴露）
   *
   * 说明：
   * 标题国际化：
   *    - titleZh / titleEn 两列，服务层会根据 Accept-Language 决定返回哪一个
   */
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Entity
  @Table(name = "menu")
  @DefaultSort(fields = {"createdAt", "id"})
  public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 父节点ID，null为根 */
    @Column(name = "parent_id")
    private Long parentId;

    /** 前端唯一 key */
    @Column(name = "menu_key", nullable = false, length = 64, unique = true)
    private String menuKey;

    /** 前端路由路径 */
    @Column(name = "path", nullable = false, length = 255)
    private String path;

    /** 标题-中文 */
    @Column(name = "title_zh", nullable = false, length = 128)
    private String titleZh;

    /** 标题-英文 */
    @Column(name = "title_en", nullable = false, length = 128)
    private String titleEn;

    /** 图标（可为空） */
    @Column(name = "icon", length = 64)
    private String icon;

    /** 排序（越小越靠前） */
    @Column(name = "order_num")
    private Integer orderNum;

    /** 显示该菜单所需的权限码（为空=默认不显示） */
    @Column(name = "required_code", length = 128)
    private String requiredCode;

    /** 审计字段（也可用 @CreationTimestamp/@UpdateTimestamp） */
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    /** 软删除标记：0=正常 1=删除 */
    @Column(name = "is_deleted", nullable = false)
    @Schema(description = "软删除标志 0=正常 1=删除")
    private Integer isDeleted = 0;

    @PreUpdate
    public void preUpdate() {
      this.updatedAt = OffsetDateTime.now();
    }
  }