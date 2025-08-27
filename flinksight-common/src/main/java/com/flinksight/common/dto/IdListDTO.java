package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Schema(description = "ID 列表请求")
public class IdListDTO {
    @NotEmpty
    @Schema(description = "ID列表")
    private List<Long> ids;
}