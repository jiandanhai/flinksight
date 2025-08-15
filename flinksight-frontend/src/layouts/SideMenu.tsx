/**
 * @file SideMenu.tsx
 * @desc 动态多级侧边菜单：支持递归、权限过滤、国际化、激活高亮、图标渲染
 */
import React from 'react';
import { useLocation, NavLink } from 'react-router-dom';
import { MENUS } from '@/constants/menus';
import { useUser } from '@/store/user';
import { useLocale } from '@/store/locale';
import type { MenuItem } from '@/constants/menus';

/**
 * SideMenu 侧边菜单组件
 */
const SideMenu: React.FC = () => {
  const { role } = useUser(); // 当前用户角色
  const { locale } = useLocale ? useLocale() : { locale: 'zh' }; // 默认中文
  const location = useLocation();

  /**
   * 判断当前菜单项是否被激活
   */
  const isMenuActive = (menu: MenuItem) => {
    const current = location.pathname;
    return current === menu.path || current.startsWith(menu.path + '/');
  };

  /**
   * 递归渲染菜单项
   */
  const renderMenuItems = (menus: MenuItem[], depth = 0): React.ReactNode =>
    menus
      .filter(menu => !menu.roles || menu.roles.includes(role)) // 权限过滤
      .map(menu => {
        const title = typeof menu.title === 'function' ? menu.title(locale) : menu.title;
        const active = isMenuActive(menu);

        return (
          <div
            key={menu.key}
            className={depth ? 'pl-4 border-l border-gray-100 dark:border-gray-700' : ''}
          >
            <NavLink
              to={menu.path || '#'}
              className={({ isActive }) =>
                `flex items-center px-4 py-2 rounded transition group
                 ${active || isActive
                   ? 'bg-blue-100 text-blue-700 font-bold dark:bg-blue-900 dark:text-white'
                   : 'text-gray-700 hover:bg-gray-100 dark:text-gray-200 dark:hover:bg-gray-800'}`
              }
            >
              {menu.icon && <span className="mr-2">{menu.icon}</span>}
              <span className="truncate">{title}</span>
            </NavLink>

            {/* 渲染子菜单 */}
            {menu.children && menu.children.length > 0 && (
              <div className="ml-2 mt-1">
                {renderMenuItems(menu.children, depth + 1)}
              </div>
            )}
          </div>
        );
      });

  return (
    <aside className="flex flex-col space-y-1 py-3 overflow-y-auto">
      {renderMenuItems(MENUS)}
    </aside>
  );
};

export default SideMenu;
