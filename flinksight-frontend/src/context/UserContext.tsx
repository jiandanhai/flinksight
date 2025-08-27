// 📁 文件路径：src/context/UserContext.tsx
// 说明：在不删除你任何原有逻辑的基础上，增强 token/首屏水合/loading/跨标签同步/refreshUser。

import React, {
  createContext,
  useContext,
  useState,
  ReactNode,
  useEffect,
  useMemo,
  useRef,
} from "react";

///src/context/UserContext.tsx

// ✅ 定义 User 类型
export interface User {
  id: number;
  tenantId: number;
  [key: string]: any;
}

// ✅ 定义上下文类型（在原有基础上，补充 token / loading / refreshUser）
interface UserContextType {
  user: User | null;
  setUser: (user: User | null) => void;
  login: (token: string) => Promise<void> | void;
  logout: () => void;

  // 🔥 新增：业务广泛依赖的字段
  token: string | null;
  loading: boolean; // 首屏水合/鉴权中的占位状态
  refreshUser: () => Promise<void>; // 主动刷新 /api/user/me
}

// ✅ 创建默认空上下文（避免 undefined 报错）
const UserContext = createContext<UserContextType | undefined>(undefined);

// === 可配置项（不改也能用） ===
const TOKEN_KEY = import.meta.env.VITE_AUTH_TOKEN_KEY || "authToken";
const ME_URL = import.meta.env.VITE_ME_URL || "/api/user/me";

/**
 * 尝试优先使用 '@/api/client' （axios 实例），失败则回退到 fetch。
 * - 项目里常见导出：export default axiosInstance 或 export const axiosInstance
 */
async function requestMe(token: string | null) {
  // 1) 优先走 axios 实例（如果存在）
  try {
    // 动态导入，避免强耦合
    const mod: any = await import("@/api/client");
    const axiosInstance = mod?.default || mod?.axiosInstance;

    if (axiosInstance?.get) {
      const res = await axiosInstance.get(ME_URL, {
        // 保险：axios 拦截器可能已自动注入 token，这里再兜底一次
        headers: token ? { Authorization: `Bearer ${token}` } : undefined,
        withCredentials: true,
      });
      // 通用兼容：data 或 data.data
      return res?.data?.data ?? res?.data ?? null;
    }
  } catch {
    // 忽略模块缺失错误，走 fetch 回退
  }

  // 2) 回退到原生 fetch（保证无 SDK 也能跑通）
  const res = await fetch(ME_URL, {
    method: "GET",
    credentials: "include",
    headers: token ? { Authorization: `Bearer ${token}` } : {},
  });
  if (!res.ok) throw new Error(`GET ${ME_URL} failed: ${res.status}`);
  const data = await res.json().catch(() => ({}));
  return (data && (data.data ?? data)) || null;
}

// ✅ Provider 组件
export const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  // 原有：仅有 user；在此基础上补充 token 与 loading
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(true); // 首屏水合阶段为 true

  // 防止 React.StrictMode 在开发环境二次执行导致的重复请求/重复跳转
  const hydratedRef = useRef<boolean>(false);

  /**
   * 首次挂载：从 sessionStorage 恢复 token，并尝试获取当前用户
   * - 全程具备容错，不会因 /api/user/me 异常而阻塞页面渲染
   */
  useEffect(() => {
    if (hydratedRef.current) return; // 严格模式防抖
    hydratedRef.current = true;

    const t = sessionStorage.getItem(TOKEN_KEY) || null;
    if (t) setToken(t);

    (async () => {
      try {
        if (t) {
          const me = await requestMe(t);
          if (me && typeof me === "object") {
            setUser(me as User);
          }
        }
      } catch (err) {
        // 获取用户信息失败时，不清 token（交由上层鉴权/跳转策略处理）
        // console.warn("[UserContext] fetch me failed:", err);
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  /**
   * 主动刷新当前用户（供 SSO 回跳后或权限变更后调用）
   */
  const refreshUser = async () => {
    try {
      setLoading(true);
      const me = await requestMe(token);
      if (me && typeof me === "object") {
        setUser(me as User);
      } else {
        setUser(null);
      }
    } finally {
      setLoading(false);
    }
  };

  /**
   * 登录：写入 token → 立即刷新用户信息
   * - 兼容你原有签名（返回 void），但这里返回 Promise<void> 方便上层 await
   */
  const login = async (newToken: string) => {
    sessionStorage.setItem(TOKEN_KEY, newToken);
    setToken(newToken);
    await refreshUser();
  };

  /**
   * 退出登录：清空 token 与 user
   */
  const logout = () => {
    sessionStorage.removeItem(TOKEN_KEY);
    setToken(null);
    setUser(null);
  };

  /**
   * 跨标签页同步（同域 sessionStorage 变更时同步本标签状态）
   * - 注意：原生 'storage' 事件不触发当前页；仅在“其它页更新”时触发
   */
  useEffect(() => {
    const onStorage = (e: StorageEvent) => {
      if (e.key && e.key !== TOKEN_KEY) return;
      const next = sessionStorage.getItem(TOKEN_KEY);
      // token 变更时同步到本页并刷新用户
      if (next !== token) {
        setToken(next);
        // 不阻塞 UI：异步刷新
        refreshUser().catch(() => void 0);
      }
    };
    window.addEventListener("storage", onStorage);
    return () => window.removeEventListener("storage", onStorage);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);

  // 组合上下文值（避免频繁重渲染）
  const ctxValue = useMemo<UserContextType>(
    () => ({
      user,
      setUser,
      login,
      logout,
      token,
      loading,
      refreshUser,
    }),
    [user, token, loading]
  );

  return <UserContext.Provider value={ctxValue}>{children}</UserContext.Provider>;
};

// ✅ 外部访问 useUser()
export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) throw new Error("useUser must be used within a UserProvider");
  return context;
};
