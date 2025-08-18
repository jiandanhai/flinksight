package com.flinksight.common.service;


import com.flinksight.common.dto.MenuNodeDTO;

import java.util.List;
import java.util.Locale;

public interface MenuService {

  /**
   * 计算某用户在指定租户下可见的菜单树（权限过滤 + 树结构 + 国际化标题）
   * 根据用户与租户返回“可见菜单树”（方案A：menu.required_code ∈ 用户权限码集合；父节点自动补齐）
   */
  List<MenuNodeDTO> getMenuTreeForUser(Long userId, Long tenantId, Locale locale);
}
