package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户-岗位关联DTO")
public class UserPostDTO  implements Serializable {
    private Long id;
    private Long userId;
    private Long postId;
    private Integer isDeleted;
}
