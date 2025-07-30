/**
 * @file 用户信息/Token管理
 * @desc 管理当前用户状态、token存储、拉取用户信息，适配SSO和本地登录
 */
import { createContext, useContext, useState, useEffect } from 'react';
import { getProfile } from '../api/profile';
import type { User } from '../types/user';

export interface UserStore {
  id: number;
  username: string;
  nickname: string;
  role: string;
  token: string;
  login: (token: string, user: User) => void;
  logout: () => void;
  refresh: () => Promise<void>;
}

const UserContext = createContext<UserStore>({} as UserStore);

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [id, setId] = useState<number>(0);
  const [username, setUsername] = useState('');
  const [nickname, setNickname] = useState('');
  const [role, setRole] = useState('');
  const [token, setToken] = useState<string>(localStorage.getItem('token') || '');

  // 登录/SSO回调
  const login = (t: string, user: User) => {
    setToken(t);
    localStorage.setItem('token', t);
    setId(user.id);
    setUsername(user.username);
    setNickname(user.nickname);
    setRole(user.role);
    // 可选：存储user对象到localStorage/sessionStorage
  };

  // 退出
  const logout = () => {
    setId(0);
    setUsername('');
    setNickname('');
    setRole('');
    setToken('');
    localStorage.removeItem('token');
    window.location.href = '/login';
  };

  // 拉取用户信息
  const refresh = async () => {
    if (!token) return;
    const res = await getProfile();
    const u = res.data;
    setId(u.id);
    setUsername(u.username);
    setNickname(u.nickname);
    setRole(u.role);
  };

  // 初始化（刷新页面自动加载用户信息）
  useEffect(() => {
    if (token && !id) refresh();
    // eslint-disable-next-line
  }, [token]);

  return (
    <UserContext.Provider value={{ id, username, nickname, role, token, login, logout, refresh }}>
      {children}
    </UserContext.Provider>
  );
};

export const useUser = () => useContext(UserContext);
