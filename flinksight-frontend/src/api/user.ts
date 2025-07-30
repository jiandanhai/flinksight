// /src/api/user.ts
import http from './http';
import type {
  User,
  UserQuery,
  UserCreateReq,
  UserUpdateReq,
  UserPasswordReq,
  UserAssignRoleReq,
  Role,
  RoleReq,
  Permission,
} from '../types/user';

/** 查询用户列表（分页、条件） */
export const getUsers = (params: UserQuery) =>
  http.get<User[]>('/users', { params });

/** 查询单个用户详情 */
export const getUserDetail = (id: number) =>
  http.get<User>(`/users/${id}`);

/** 新建用户 */
export const createUser = (data: UserCreateReq) =>
  http.post<User>('/users', data);

/** 编辑用户 */
export const updateUser = (id: number, data: UserUpdateReq) =>
  http.put<User>(`/users/${id}`, data);

/** 删除用户 */
export const deleteUser = (id: number) =>
  http.delete(`/users/${id}`);

/** 批量删除用户 */
export const deleteUsers = (ids: number[]) =>
  http.post('/users/batch-delete', { ids });

/** 启用/禁用用户 */
export const setUserStatus = (id: number, status: UserStatus) =>
  http.post(`/users/${id}/status`, { status });

/** 用户修改密码 */
export const updateUserPassword = (id: number, data: UserPasswordReq) =>
  http.post(`/users/${id}/password`, data);

/** 用户强制重置密码（管理员） */
export const resetUserPassword = (id: number, newPassword: string) =>
  http.post(`/users/${id}/reset-password`, { newPassword });

/** 用户分配角色 */
export const assignUserRoles = (data: UserAssignRoleReq) =>
  http.post('/users/assign-roles', data);

/** 查询角色列表 */
export const getRoles = () =>
  http.get<Role[]>('/roles');

/** 查询单个角色详情 */
export const getRoleDetail = (id: number) =>
  http.get<Role>(`/roles/${id}`);

/** 新建角色 */
export const createRole = (data: RoleReq) =>
  http.post<Role>('/roles', data);

/** 编辑角色 */
export const updateRole = (id: number, data: Partial<RoleReq>) =>
  http.put<Role>(`/roles/${id}`, data);

/** 删除角色 */
export const deleteRole = (id: number) =>
  http.delete(`/roles/${id}`);

/** 查询全部权限点列表 */
export const getPermissions = () =>
  http.get<Permission[]>('/permissions');

/** 查询角色权限 */
export const getRolePermissions = (roleId: number) =>
  http.get<Permission[]>(`/roles/${roleId}/permissions`);

/** 更新角色权限 */
export const updateRolePermissions = (roleId: number, permissionIds: number[]) =>
  http.post(`/roles/${roleId}/permissions`, { permissionIds });

    