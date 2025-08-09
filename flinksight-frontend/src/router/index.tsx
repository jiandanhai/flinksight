import React, { Suspense } from 'react';
import { Navigate, Route, Routes } from 'react-router-dom';
import AuthRoute from '../components/AuthRoute/AuthRoute';
import MainLayout from '../layouts/MainLayout';
import LoginPage from '../pages/Login';
import SSOCallback from '../pages/Login/SSOCallback';
import type { MenuItem } from '../constants/menus';
import { MENUS } from '../constants/menus';
import { useUser } from '../store/user';
import { useLocale } from '../store/locale'; // 动态获取当前语言环境
import { loadMicroApp } from '../microfrontends/loader'; // 微前端挂载器

const Pages: Record<string, React.LazyExoticComponent<React.FC>> = {
  DashboardPage: React.lazy(() => import('../pages/Dashboard')),
  ClusterPage: React.lazy(() => import('../pages/Cluster')),
  NodePage: React.lazy(() => import('../pages/Cluster/Node')),
  JobPage: React.lazy(() => import('../pages/Job')),
  AlertPage: React.lazy(() => import('../pages/Alert')),
  RulePage: React.lazy(() => import('../pages/Alert/Rule')),
  UserPage: React.lazy(() => import('../pages/User')),
  RolePage: React.lazy(() => import('../pages/Role')),
  TenantPage: React.lazy(() => import('../pages/Tenant')),
  SettingsPage: React.lazy(() => import('../pages/Settings')),
  OpsPage: React.lazy(() => import('../pages/Ops')),
  // ...扩展其它本地页面
};

const NotFound: React.FC = () => <div>404 Not Found</div>;

// 递归生成多级路由与微前端接入
function renderRoutes(menus: MenuItem[], locale: string, role: string): React.ReactNode[] {
  return menus
    .filter(menu => !menu.roles || menu.roles.includes(role))
    .map(menu => {
      if (menu.microApp) {
        // 微前端子应用集成，推荐qiankun/Module Federation等方案
        return (
          <Route
            key={menu.key}
            path={menu.path + '/*'}
            element={loadMicroApp(menu.microApp)}
          />
        );
      }
      // 本地页面
      const Element = menu.element && Pages[menu.element] ? Pages[menu.element] : NotFound;
      return (
        <Route key={menu.key} path={menu.path + '/*'} element={
          <Suspense fallback={<div>页面加载中...</div>}>
            <Element />
          </Suspense>
        }>
          {/* 递归嵌套路由 */}
          {menu.children && renderRoutes(menu.children, locale, role)}
        </Route>
      );
    });
}

const AppRoutes: React.FC = () => {
  const { role } = useUser();
  const { locale } = useLocale(); // 支持国际化

  // 过滤菜单/路由（role权限自动过滤）
  const routes = MENUS.filter(menu => !menu.roles || menu.roles.includes(role));

  return (
    <Routes>
      {/* ✅ 白名单在守卫外，且把回调放在 /login 前更稳 */}
      <Route path="/login/sso-callback" element={<SSOCallback />} />
      <Route path="/login" element={<LoginPage />} />
      {/* ✅ 受保护区域 */}
      <Route element={<AuthRoute />}>
        <Route element={<MainLayout />}>
          {renderRoutes(routes, locale, role)}
          {/* 根路由重定向到第一个有权限页面 */}
          <Route path="/" element={<Navigate to={routes[0]?.path || '/login'} />} />
        </Route>
      </Route>
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
