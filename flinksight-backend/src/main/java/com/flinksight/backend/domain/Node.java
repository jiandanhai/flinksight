package com.flinksight.backend.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 节点/主机/Agent表
 */
@Data
@Entity
@Table(name = "node")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "节点表")
@Where(clause = "is_deleted=0")
public class Node implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "节点ID")
    private Long id;

    @Schema(description = "节点名称")
    private String name;

    @Schema(description = "节点类型")
    private String type;

    @Schema(description = "节点IP")
    private String ip;

    @Schema(description = "关联集群ID")
    private Long clusterId;

    @Schema(description = "节点状态")
    private Integer status;

    @Schema(description = "软删除标志")
    private Integer isDeleted = 0;

    @Schema(description = "注册时间")
    private LocalDateTime createTime;
}
