// 📁 src/main.tsx

import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import App from "./App";
import { UserProvider } from "./context/UserContext"; // ✅ 确保引入了 Provider
//通用网络抓手（fetch + XHR 双兜底）只在开发环境启用；不会消费响应体（用了 clone()）
import { installDevNetTap } from '@/utils/devNetTap';
if (import.meta.env.DEV) installDevNetTap();
//

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <BrowserRouter>
      <UserProvider>
        <App />
      </UserProvider>
    </BrowserRouter>
  </React.StrictMode>
);
