/**
 * @file SideMenu.tsx
 * @desc 动态多级侧边菜单，点击“作业列表”时自动补 clusterId。
 */
import React from 'react';
import { useLocation, NavLink, useNavigate } from 'react-router-dom';
import { MENUS } from '@/constants/menus';
import { useUser } from '@/store/user';
import { useLocale } from '@/store/locale';
import type { MenuItem } from '@/constants/menus';
import { useCluster } from '@/context/ClusterContext';

const SideMenu: React.FC = () => {
  const { role } = useUser();
  const { locale } = useLocale ? useLocale() : { locale: 'zh' };
  const location = useLocation();
  const nav = useNavigate();
  const { id: currentClusterId } = useCluster();

  const isMenuActive = (menu: MenuItem) => {
    const current = location.pathname;
    return current === menu.path || current.startsWith(menu.path + '/');
  };

  const renderMenuItems = (menus: MenuItem[], depth = 0): React.ReactNode =>
    menus
      .filter(menu => !menu.roles || menu.roles.includes(role))
      .map(menu => {
        const title = typeof menu.title === 'function' ? menu.title(locale) : menu.title;
        const active = isMenuActive(menu);
        const to =
          menu.path === '/job'
            ? (currentClusterId ? `/job?clusterId=${currentClusterId}` : '/cluster')
            : (menu.path || '#');

        return (
          <div
            key={menu.key}
            className={depth ? 'pl-4 border-l border-gray-100 dark:border-gray-700' : ''}
          >
            <NavLink
              to={to}
              onClick={(e) => {
                if (menu.path === '/job' && !currentClusterId) {
                  e.preventDefault();
                  nav('/cluster');
                }
              }}
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
