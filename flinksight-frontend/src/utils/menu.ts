// 📁 src/utils/menu.ts
// ✅ 从角色权限列表中过滤出侧边菜单（从 menus.ts 中引入）

import { MENUS } from '@/constants/menus';
import type { MenuItem } from '@/constants/menus';

export function getMenusByPermission(permissions: string[]): MenuItem[] {
  // 递归过滤菜单项
  const filterMenus = (menus: MenuItem[]): MenuItem[] => {
    return menus
      .filter(menu => !menu.roles || menu.roles.some(role => permissions.includes(role)))
      .map(menu => ({
        ...menu,
        children: menu.children ? filterMenus(menu.children) : undefined
      }));
  };

  return filterMenus(MENUS);
}
