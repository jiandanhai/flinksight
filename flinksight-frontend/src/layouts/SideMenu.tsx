/**
 * @file SideMenu.tsx
 * @desc 动态多级侧边菜单，自动聚合、角色权限过滤、国际化、图标、激活高亮，适配 MENUS 多场景
 */
import React from 'react';
import {MENUS} from '../constants/menus';
import {useUser} from '../store/user';
import {useLocale} from '../store/locale';
import {NavLink, useLocation} from 'react-router-dom';
import type {MenuItem} from '../types/menu';

/**
 * 动态递归多级菜单
 */
const SideMenu: React.FC = () => {
  const { role } = useUser();
  const { locale } = useLocale ? useLocale() : { locale: 'zh' }; // 兼容无国际化
  const location = useLocation();

  // 递归渲染菜单
  const renderMenus = (menus: MenuItem[], depth = 0) =>
    menus
      .filter(menu => !menu.roles || menu.roles.includes(role))
      .map(menu => {
        // 国际化菜单标题支持
        const title =
          typeof menu.title === 'function'
            ? menu.title(locale)
            : menu.title;

        // 是否当前路径激活
        const isActive =
          location.pathname === menu.path ||
          location.pathname.startsWith(menu.path + '/');

        return (
          <div key={menu.key} className={depth ? 'pl-4 border-l border-gray-100' : ''}>
            <NavLink
              to={menu.path || '#'}
              className={({ isActive: navActive }) =>
                `flex items-center px-4 py-2 rounded transition group
                ${isActive || navActive
                  ? 'bg-blue-100 text-blue-700 font-bold'
                  : 'text-gray-700 hover:bg-gray-100'}
                `
              }
            >
              {menu.icon && <span className="mr-2">{menu.icon}</span>}
              <span>{title}</span>
            </NavLink>
            {/* 多级菜单递归 */}
            {menu.children && menu.children.length > 0 && (
              <div>
                {renderMenus(menu.children, depth + 1)}
              </div>
            )}
          </div>
        );
      });

  return (
    <nav className="flex flex-col space-y-1 py-2">
      {renderMenus(MENUS)}
    </nav>
  );
};

export default SideMenu;
