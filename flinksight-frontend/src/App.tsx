// /src/App.tsx
import React from "react";
import Router from "./router";

/**
 * App 作为路由入口容器。
 * 说明：
 * - 全局 UserProvider 已在 /src/main.tsx 中由 ./context/UserContext 包裹；
 * - 这里不再重复包第二层 Provider，避免上下文不一致导致的鉴权/菜单异常。
 */
const App: React.FC = () => {
  return <Router />;
};

export default App;
