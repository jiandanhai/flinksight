// src/main/java/com/yourapp/menu/dto/MenuNodeDTO.java
package com.flinksight.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuNodeDTO implements Serializable {
    private Long id;
    private String key;
    private String path;
    private String title;
    private String icon;
    private Integer orderNum;

    @Builder.Default
    private List<MenuNodeDTO> children = new ArrayList<>();
}
