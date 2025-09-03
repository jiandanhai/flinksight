// src/main/java/com/flinksight/backend/dto/IdListRequest.java
package com.flinksight.common.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IdListRequestDTO {
  @NotEmpty
  private List<Long> ids;
}
