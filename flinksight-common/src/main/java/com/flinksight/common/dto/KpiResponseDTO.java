package com.flinksight.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KpiResponseDTO implements Serializable {
    private List<String> date;   // yyyy-MM-dd
    private List<Integer> active; // “活跃” -> 这里用每日创建任务数
    private List<Integer> paid;   // “付费” -> 这里用每日成功任务数
}