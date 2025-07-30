/**
 * 操作日志实体
 */
export interface OperationLog {
  id: number;
  user: string;
  action: string;
  target: string;
  detail: string;
  ip: string;
  time: string;
}

/**
 * 操作日志查询参数
 */
export interface OperationLogQuery {
  user?: string;
  action?: string;
  page?: number;
  size?: number;
}
