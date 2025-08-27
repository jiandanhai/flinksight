package com.flinksight.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FunnelResponseDTO implements Serializable {
    private List<String> steps;   // ["已创建","运行中","成功","失败"]（可按需扩展）
    private List<Integer> values; // [countCreated,countRunning,countSuccess,countFailed]
}