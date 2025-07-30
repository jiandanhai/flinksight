import { useContext, createContext } from 'react';
/**
 * 用户登录信息hook（仅示意，生产用JWT或更严格方案）
 */
const AuthContext = createContext<{ isLogin: boolean; user?: any }>({ isLogin: false });

export function useAuth() {
  return useContext(AuthContext);
}

// 生产环境建议包裹Provider实现持久化和自动刷新
