/**
 * 角色实体
 */
export interface Role {
  id: number;
  name: string;
  code: string;
  desc?: string;
  permissions: string[]; // 权限点列表
  createTime: string;
}

/**
 * 查询角色参数
 */
export interface RoleQuery {
  keyword?: string;
  page?: number;
  size?: number;
}

/**
 * 创建角色参数
 */
export interface RoleCreateReq {
  name: string;
  code: string;
  desc?: string;
  permissions: string[];
}

/**
 * 更新角色参数
 */
export interface RoleUpdateReq {
  name?: string;
  code?: string;
  desc?: string;
  permissions?: string[];
}
