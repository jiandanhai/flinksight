// /src/types/user.ts 用户、角色、权限相关全部类型定义，含授权、编辑、批量等

/**
 * 用户状态
 */
export type UserStatus = 1 | 0; // 1启用，0禁用

/**
 * 用户信息
 */
export interface User {
  id: number;
  tenantId?: number;
  username: string;
  email?: string;
  phone?: string;
  status: UserStatus;
  roles: Role[];
  isDeleted?: 0 | 1;
  createTime: string;
  updateTime: string;
}

/**
 * 用户查询参数
 */
export interface UserQuery {
  username?: string;
  email?: string;
  phone?: string;
  status?: UserStatus;
  roleId?: number;
  page?: number;
  size?: number;
}

/**
 * 新建用户请求
 */
export interface UserCreateReq {
  username: string;
  password: string;
  email?: string;
  phone?: string;
  roleIds?: number[];
}

/**
 * 修改用户请求
 */
export interface UserUpdateReq {
  email?: string;
  phone?: string;
  status?: UserStatus;
  roleIds?: number[];
}

/**
 * 修改密码请求
 */
export interface UserPasswordReq {
  oldPassword?: string;
  newPassword: string;
}

/**
 * 角色信息
 */
export interface Role {
  id: number;
  name: string;
  code: string;
  desc?: string;
}

/**
 * 新建/编辑角色请求
 */
export interface RoleReq {
  name: string;
  code: string;
  desc?: string;
  permissionIds?: number[];
}

/**
 * 权限信息
 */
export interface Permission {
  id: number;
  code: string;
  name: string;
  desc?: string;
}

/**
 * 用户授权分配
 */
export interface UserAssignRoleReq {
  userId: number;
  roleIds: number[];
}
