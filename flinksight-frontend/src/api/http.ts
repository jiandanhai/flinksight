/**
 * @file 统一请求封装
 * @desc 集中处理全局请求、Token、错误提示，可拓展拦截器
 */
import axios from 'axios';
import { message } from 'antd';
import { useTenant } from '../store/tenant';

const http = axios.create({ baseURL: process.env.REACT_APP_API_URL });

// 请求拦截器：自动带token
http.interceptors.request.use(
  (config) => {
    const tenantId = localStorage.getItem('tenantId');
    if (tenantId) config.headers['X-Tenant-Id'] = tenantId;
    const token = localStorage.getItem('token');
    if (token) {
      config.headers!['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (err) => Promise.reject(err)
);

// 响应拦截器：统一错误处理
http.interceptors.response.use(
  res => res.data,
  error => {
    if (error.response) {
      const { status, data } = error.response;
      if (status === 401) {
        message.error('登录过期，请重新登录');
        localStorage.removeItem('token');
        setTimeout(() => window.location.href = '/login', 100);
        return Promise.reject(new Error('登录失效'));
      }
      if (status === 403) {
        message.error('没有权限执行此操作');
        return Promise.reject(new Error('无权限'));
      }
      message.error(data.message || `接口异常 [${status}]`);
    } else if (error.request) {
      message.error('网络异常，请检查您的连接');
    } else {
      message.error('系统异常，请稍后重试');
    }
    return Promise.reject(error);
  }
);

export default http;
