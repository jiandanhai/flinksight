// src/main/java/com/flinksight/backend/dto/BatchEnableRequest.java
package com.flinksight.common.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BatchEnableRequestDTO {
  @NotEmpty
  private List<Long> ids;

  @NotNull @Min(0) @Max(1)
  private Integer enabled;
}
