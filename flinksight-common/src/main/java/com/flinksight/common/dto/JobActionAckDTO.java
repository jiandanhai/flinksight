package com.flinksight.common.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobActionAckDTO {
  private boolean accepted;
  private String taskId;    // 异步任务 id（重启/重提通常需走任务化）
  private String message;
}