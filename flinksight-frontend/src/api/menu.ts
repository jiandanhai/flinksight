    // /src/api/menu.ts
import http from './http';
import type { MenuItem } from '../types/menu';

/** 查询所有菜单/权限点 */
export const getMenus = () =>
  http.get<MenuItem[]>('/menus');

/** 查询当前用户可用菜单 */
export const getUserMenus = () =>
  http.get<MenuItem[]>('/menus/user');
