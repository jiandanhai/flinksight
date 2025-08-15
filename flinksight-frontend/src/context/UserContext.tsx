// 📁 文件路径：src/context/UserContext.tsx

import React, { createContext, useContext, useState, ReactNode } from "react";

// ✅ 定义 User 类型
export interface User {
  id: number;
  tenantId: number;
  [key: string]: any;
}

// ✅ 定义上下文类型
interface UserContextType {
  user: User | null;
  setUser: (user: User | null) => void;
  login: (token: string) => void;
  logout: () => void;
}

// ✅ 创建默认空上下文（避免 undefined 报错）
const UserContext = createContext<UserContextType | undefined>(undefined);

// ✅ Provider 组件
export const UserProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);

  const login = (token: string) => {
    sessionStorage.setItem("authToken", token);
    // 如果有用户信息接口，可以在这里请求并 setUser
  };

  const logout = () => {
    sessionStorage.removeItem("authToken");
    setUser(null);
  };

  return (
    <UserContext.Provider value={{ user, setUser, login, logout }}>
      {children}
    </UserContext.Provider>
  );
};

// ✅ 外部访问 useUser()
export const useUser = (): UserContextType => {
  const context = useContext(UserContext);
  if (!context) throw new Error("useUser must be used within a UserProvider");
  return context;
};
