import React, { Suspense } from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import AuthRoute from "src/components/AuthRoute/AuthRoute";
import MainLayout from "../layouts/MainLayout";
import LoginPage from "../pages/Login";
import SSOCallback from "../pages/Login/SSOCallback";

// 🔧 这里改用你提供的 routes（包含 /dashboard）。如果你的文件路径不同，请调整！
import routes from "./routers";
import type { RouteMeta } from "./routers";

const NotFound: React.FC = () => <div>404 Not Found</div>;

function render(routesMeta: RouteMeta[]): React.ReactNode[] {
  return routesMeta.map((m) => {
    const Element = m.component ?? NotFound;
    if (m.children?.length) {
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
      {/* ✅ 白名单放在守卫外 */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/login/sso-callback" element={<SSOCallback />} />

      {/* ✅ 受保护区 */}
      <Route element={<AuthRoute />}>
        <Route element={<MainLayout />}>
          {/* 关键：把你提供的 routes 全量挂上来，保证 /dashboard 存在 */}
          <Route
            path="/"
            element={
              <Suspense fallback={<div>页面加载中…</div>}>
                <Navigate to="/dashboard" replace />
              </Suspense>
            }
          />
          {render(routes)}
        </Route>
      </Route>

      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default AppRoutes;
