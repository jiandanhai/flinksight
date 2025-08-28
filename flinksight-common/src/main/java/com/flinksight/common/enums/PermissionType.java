package com.flinksight.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PermissionType {
    MENU("MENU", "菜单"),
    BUTTON("BUTTON", "按钮"),
    API("API", "接口");

    private final String code;
    private final String desc;


}
