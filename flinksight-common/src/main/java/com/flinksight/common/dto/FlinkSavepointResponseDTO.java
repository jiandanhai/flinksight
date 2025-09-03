package com.flinksight.common.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FlinkSavepointResponseDTO {
  private boolean accepted;
  private String location;   // 成功时的 savepoint 目录
  private String message;    // 摘要
}