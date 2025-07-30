// /src/types/common.ts 通用分页与统一响应类型（项目通用

/**
 * 通用分页返回
 */
export interface Page<T> {
  records: T[];
  total: number;
  page: number;
  size: number;
}

/**
 * 统一API响应类型
 */
export interface ApiResponse<T = any> {
  code: number;
  msg: string;
  data: T;
}
