import React, {
  createContext,
  useContext,
  useEffect,
  useMemo,
  useState,
  PropsWithChildren,
} from 'react';
import { setApiToken } from '@/api/gen/client';

// 用户结构：根据你后端 JWT 中解析信息适配
interface UserInfo {
  id: number;
  username: string;
  role: string;
  tenantId?: number;
}

interface UserContextType {
  token: string | null;
  userInfo: UserInfo | null;
  hydrated: boolean;
  login: (token: string) => void;
  logout: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error('useUser must be used within a UserProvider');
  }
  return context;
};

// 从 token 解码出用户信息（示例使用 Base64 解析 JWT payload）
const parseUserFromToken = (token: string): UserInfo | null => {
  try {
    const base64Payload = token.split('.')[1];
    const payload = JSON.parse(atob(base64Payload));
    return {
      id: payload.userId,
      username: payload.username,
      role: payload.role,
      tenantId: payload.tenantId,
    };
  } catch {
    return null;
  }
};

export const UserProvider: React.FC<PropsWithChildren> = ({ children }) => {
  const [token, setToken] = useState<string | null>(null);
  const [userInfo, setUserInfo] = useState<UserInfo | null>(null);
  const [hydrated, setHydrated] = useState(false);

  useEffect(() => {
    const t =
      (typeof window !== 'undefined' &&
        (sessionStorage.getItem('authToken') || localStorage.getItem('authToken'))) ||
      null;

    if (t) {
      setToken(t);
      setApiToken(t);
      setUserInfo(parseUserFromToken(t));
    } else {
      setApiToken(undefined);
    }
    setHydrated(true);
  }, []);

  const login = (t: string) => {
    setToken(t);
    setApiToken(t);
    const user = parseUserFromToken(t);
    setUserInfo(user);
    try {
      sessionStorage.setItem('authToken', t);
      localStorage.setItem('authToken', t);
    } catch {}
  };

  const logout = () => {
    setToken(null);
    setUserInfo(null);
    setApiToken(undefined);
    try {
      sessionStorage.removeItem('authToken');
      localStorage.removeItem('authToken');
    } catch {}
  };

  const value = useMemo(
    () => ({ token, userInfo, hydrated, login, logout }),
    [token, userInfo, hydrated]
  );

  return <UserContext.Provider value={value}>{children}</UserContext.Provider>;
};
