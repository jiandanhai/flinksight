// src/store/user.tsx
/**
 * 兼容桥接层（重要说明）
 * - 保留原有导出：useUser、UserProvider、UserContextType、UserInfo、parseUserFromToken
 * - 实现改为转发到唯一真实上下文：/src/context/UserContext.tsx
 * - 解决因“双 Context”导致的：`useUser must be used within a UserProvider`
 */

import React, { PropsWithChildren, useMemo } from "react";

// ⬇️ 统一使用同一套真正的上下文
import {
  useUser as useCtxUser,
  UserProvider as CtxUserProvider,
  type User as CtxUser,
} from "@/context/UserContext";

// ======（保留你原有的类型定义）======
// 用户结构：根据你后端 JWT 中解析信息适配
export interface UserInfo {
  id: number;
  username: string;
  role: string;
  tenantId?: number;
}

// 旧上下文类型（保持对外兼容）
// - token: 与 context 中 token 对齐
// - userInfo: 从 context.user 映射得到
// - hydrated: 首屏水合是否完成（= !context.loading）
export interface UserContextType {
  token: string | null;
  userInfo: UserInfo | null;
  hydrated: boolean;
  login: (token: string) => void | Promise<void>;
  logout: () => void;
}

/**
 * 兼容：从 token 解码用户信息（保留旧函数，不强制使用）
 * - 某些业务可能直接调用该函数，故继续导出
 */
export const parseUserFromToken = (token: string): UserInfo | null => {
  try {
    const base64Payload = token.split(".")[1];
    const payload = JSON.parse(atob(base64Payload));
    return {
      id: payload.userId ?? payload.id,
      username: payload.username ?? payload.name ?? "",
      role: payload.role ?? (Array.isArray(payload.roles) ? payload.roles[0] : ""),
      tenantId: payload.tenantId,
    };
  } catch {
    return null;
  }
};

/**
 * 【兼容版】useUser
 * - 转发到 context.UserContext，并做字段映射以保持老代码可用
 */
export const useUser = (): UserContextType => {
  const ctx = useCtxUser(); // 来自 /src/context/UserContext.tsx

  // 将 context.user 映射为老的 userInfo 形状
  const userInfo: UserInfo | null = useMemo(() => {
    const u = ctx.user as CtxUser | null;
    if (!u) return null;
    return {
      id: (u as any).id,
      username: (u as any).username ?? (u as any).name ?? "",
      role:
        (u as any).role ??
        ((Array.isArray((u as any).roles) && (u as any).roles[0]) || ""),
      tenantId: (u as any).tenantId,
    };
  }, [ctx.user]);

  // hydrated：首屏水合完成（与 context.loading 取反）
  const hydrated = !ctx.loading;

  return {
    token: ctx.token,
    userInfo,
    hydrated,
    // 直接转发（context.login 会自动刷新用户）
    login: ctx.login,
    logout: ctx.logout,
  };
};

/**
 * 【兼容版】UserProvider
 * - 直接渲染 context 的 Provider，避免出现第二套 Provider
 * - 这样无论旧代码从 '@/store/user' 还是新代码从 '@/context/UserContext' 导入，指向同一 Provider
 */
export const UserProvider: React.FC<PropsWithChildren> = ({ children }) => {
  return <CtxUserProvider>{children}</CtxUserProvider>;
};
