/**
 * 租户实体
 */
export interface Tenant {
  id: number;
  name: string;
  code: string;
  desc?: string;
  status: 0 | 1; // 0禁用 1启用
  createTime: string;
}

/**
 * 查询租户参数
 */
export interface TenantQuery {
  keyword?: string;
  page?: number;
  size?: number;
}

/**
 * 创建租户参数
 */
export interface TenantCreateReq {
  name: string;
  code: string;
  desc?: string;
  status?: 0 | 1;
}

/**
 * 更新租户参数
 */
export interface TenantUpdateReq {
  name?: string;
  code?: string;
  desc?: string;
  status?: 0 | 1;
}
