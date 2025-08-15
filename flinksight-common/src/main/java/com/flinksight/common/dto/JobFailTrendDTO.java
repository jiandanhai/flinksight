package com.flinksight.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@AllArgsConstructor
public class JobFailTrendDTO  implements Serializable {

    @Schema(description = "每天失败率 Map<日期, 百分比>")
    private Map<String, Double> failRateByDate;
}