import React, { createContext, useContext, useState, ReactNode } from 'react';

interface AuthContextType {
  isLogin: boolean;
  user?: any;
  setLogin: (isLogin: boolean, user?: any) => void;
}

// 1. 创建 Context
export const AuthContext = createContext<AuthContextType>({
  isLogin: false,
  user: undefined,
  setLogin: () => {},
});

// 2. 提供 Provider 组件，包裹在 App 根部
export const AuthProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [isLogin, setIsLogin] = useState<boolean>(!!localStorage.getItem('token'));
  const [user, setUser] = useState<any>(null);

  const setLogin = (login: boolean, userObj?: any) => {
    setIsLogin(login);
    setUser(userObj);
    if (login) {
      localStorage.setItem('token', userObj?.token ?? '');
    } else {
      localStorage.removeItem('token');
    }
  };

  return (
    <AuthContext.Provider value={{ isLogin, user, setLogin }}>
      {children}
    </AuthContext.Provider>
  );
};

// 3. 快捷 hook
export function useAuth() {
  return useContext(AuthContext);
}
