// /src/router/index.tsx
import React, { Suspense } from "react";
import { Navigate, Route, Routes, Outlet } from "react-router-dom";
import AuthRoute from "@/components/AuthRoute/AuthRoute"; // ✅ 统一使用 @ -> /src 的别名（见下方 tsconfig 提示）
import MainLayout from "../layouts/MainLayout";
import LoginPage from "../pages/Login";
import SSOCallback from "../pages/Login/SSOCallback";

import routes from "./routers";
import type { RouteMeta } from "./routers";

/**
 * 404 兜底页
 * 说明：只有当确实没有命中任何 Route 时，才会进入这里
 */
const NotFound: React.FC = () => <div>404 Not Found</div>;

/**
 * 将 RouteMeta 数组渲染为 <Route/> 列表。
 *
 * 【关键修复】
 * - 当某个路由项「有 children 但没有 component」时，父级必须渲染 <Outlet/>
 *   否则其子路由不会出现，这正是你“二级菜单除了大屏其它都是 404”的根因。
 *
 * 【保留设计】
 * - 如果提供了 component，就优先渲染 component；
 * - 如果没提供 component 且也没有 children，则渲染 NotFound（兜底）。
 */
function render(routesMeta: RouteMeta[]): React.ReactNode[] {
  return routesMeta.map((m) => {
    const hasChildren = Array.isArray(m.children) && m.children.length > 0;

    // ✅ 核心：父路由无 component 但有 children -> 用 <Outlet/> 占位
    const Element =
      (m.component as React.FC | undefined) ?? (hasChildren ? Outlet : NotFound);

    if (hasChildren) {
      return (
        <Route key={m.path} path={m.path} element={<Element />}>
          {render(m.children)}
        </Route>
      );
    }

    return <Route key={m.path} path={m.path} element={<Element />} />;
  });
}

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      {/* ✅ 白名单（无需鉴权） */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/login/sso-callback" element={<SSOCallback />} />

      {/* ✅ 鉴权区域（需要登录） */}
      <Route element={<AuthRoute />}>
        {/* ✅ 业务主框架 */}
        <Route element={<MainLayout />}>
          {/* 默认重定向到 /dashboard（保留你的行为） */}
          <Route
            path="/"
            element={
              <Suspense fallback={<div>页面加载中…</div>}>
                <Navigate to="/dashboard" replace />
              </Suspense>
            }
          />
          {/* ✅ 渲染你在 /src/router/routers.tsx 定义的所有路由 */}
          {render(routes)}
        </Route>
      </Route>

      {/* ✅ 全局兜底 404 */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
