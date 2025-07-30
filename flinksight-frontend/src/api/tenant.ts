import http from './http';
import type { Tenant, TenantQuery, TenantCreateReq, TenantUpdateReq } from '../types/tenant';

/**
 * 查询租户列表
 */
export const getTenants = (params: TenantQuery) => http.get<Tenant[]>('/tenants', { params });

/**
 * 获取租户详情
 */
export const getTenantDetail = (id: number) => http.get<Tenant>(`/tenants/${id}`);

/**
 * 新建租户
 */
export const createTenant = (data: TenantCreateReq) => http.post('/tenants', data);

/**
 * 更新租户
 */
export const updateTenant = (id: number, data: TenantUpdateReq) => http.put(`/tenants/${id}`, data);

/**
 * 删除租户
 */
export const deleteTenant = (id: number) => http.delete(`/tenants/${id}`);

/**
 * 批量删除租户
 */
export const deleteTenants = (ids: number[]) => http.post('/tenants/batch-delete', { ids });
