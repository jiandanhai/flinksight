// /src/components/AuthRoute/AuthRoute.tsx
import React from "react";
import { Outlet, useLocation } from "react-router-dom";
// 🚨 关键：与 main.tsx 保持同一套上下文，避免双 Provider 导致状态不一致
import { useUser } from "@/context/UserContext";

/**
 * 说明：
 * - 仍然保留你的后端 SSO 登录入口（通过环境变量可配置）
 * - 白名单包含登录页与回调页
 * - 未登录时做「硬跳转」到后端 SSO，并带回当前完整地址
 */
const SSO_LOGIN_URL =
  import.meta.env.VITE_SSO_LOGIN_URL || "/api/sso/sso-login";

// ✅ 回调白名单：这些路径不做鉴权拦截
const ALLOWLIST = new Set<string>(["/login", "/login/sso-callback"]);

const AuthRoute: React.FC = () => {
  const { user, token, loading } = useUser(); // 从同一上下文拿 user/token/loading
  const location = useLocation();

  // React 18 StrictMode 下函数组件可能在 DEV 渲染两次，这里防止重复发起硬跳转
  const didRedirect = React.useRef(false);

  // 1) 白名单路径：直接放行（保证回调流程/登录页正常）
  if (ALLOWLIST.has(location.pathname)) {
    return <Outlet />;
  }

  // 2) 全局加载态：建议显示骨架屏/空占位，避免闪烁
  if (loading) {
    return null; // 或者 return <FullScreenLoading />
  }

  // 3) 已登录：放行受保护区域
  //    既可依赖 token，也可更稳妥依赖 user.id（后端 /api/user/me 成功后的结果）
  if (token || (user && user.id)) {
    return <Outlet />;
  }

  // 4) 未登录：跳转后端 SSO，带当前完整地址
  if (!didRedirect.current) {
    didRedirect.current = true;
    const redirect = encodeURIComponent(window.location.href);
    // 硬跳转（服务端统一处理登录并回跳）
    window.location.href = `${SSO_LOGIN_URL}?redirect=${redirect}`;
  }

  // 返回空：中断当前渲染（防止在跳转前再次渲染）
  return null;
};

export default AuthRoute;
