import http from './http';
import type { Role, RoleQuery, RoleCreateReq, RoleUpdateReq } from '../types/role';

/**
 * 查询角色列表
 * @param params RoleQuery
 * @returns Promise<Role[]>
 */
export const getRoles = (params: RoleQuery) => http.get<Role[]>('/roles', { params });

/**
 * 获取角色详情
 * @param id 角色ID
 */
export const getRoleDetail = (id: number) => http.get<Role>(`/roles/${id}`);

/**
 * 新建角色
 * @param data RoleCreateReq
 */
export const createRole = (data: RoleCreateReq) => http.post('/roles', data);

/**
 * 更新角色
 * @param id 角色ID
 * @param data RoleUpdateReq
 */
export const updateRole = (id: number, data: RoleUpdateReq) => http.put(`/roles/${id}`, data);

/**
 * 删除角色
 * @param id 角色ID
 */
export const deleteRole = (id: number) => http.delete(`/roles/${id}`);

/**
 * 批量删除角色
 * @param ids 角色ID数组
 */
export const deleteRoles = (ids: number[]) => http.post('/roles/batch-delete', { ids });
