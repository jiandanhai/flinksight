import http from './http';
import type { Profile, ProfileUpdateReq, PasswordUpdateReq, LoginLog } from '../types/profile';

/**
 * 获取当前用户个人资料
 */
export const getProfile = () => http.get<Profile>('/user/profile');

/**
 * 更新个人资料
 */
export const updateProfile = (data: ProfileUpdateReq) => http.put('/user/profile', data);

/**
 * 修改密码
 */
export const updatePassword = (data: PasswordUpdateReq) => http.post('/user/password', data);

/**
 * 查询登录日志
 */
export const getLoginLogs = () => http.get<LoginLog[]>('/user/login-logs');
