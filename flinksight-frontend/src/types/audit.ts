// /src/types/audit.ts 审计日志相关类型

/**
 * 操作审计日志
 */
export interface AuditLog {
  id: number;
  tenantId?: number;
  userId?: number;
  action: string;
  targetType?: string;
  targetId?: number;
  ip?: string;
  content?: string;
  createTime: string;
}

/**
 * 审计日志查询参数
 */
export interface AuditLogQuery {
  userId?: number;
  action?: string;
  targetType?: string;
  targetId?: number;
  from?: string;
  to?: string;
  page?: number;
  size?: number;
}
