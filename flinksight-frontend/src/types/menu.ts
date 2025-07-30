// /src/types/menu.ts 菜单/权限点类型（如前端权限路由/菜单中心）

/**
 * 菜单/权限点信息
 */
export interface MenuItem {
  id: number;
  name: string;
  code: string;
  path: string;
  icon?: string;
  parentId?: number;
  children?: MenuItem[];
  order?: number;
  type?: 'menu' | 'button';
  hidden?: boolean;
}
 