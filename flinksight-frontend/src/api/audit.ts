// /src/api/audit.ts
import http from './http';
import type { AuditLog, AuditLogQuery } from '../types/audit';

/** 查询审计日志列表（分页、条件） */
export const getAuditLogs = (params: AuditLogQuery) =>
  http.get<AuditLog[]>('/audit_logs', { params });

/** 查询单条审计日志详情 */
export const getAuditLogDetail = (id: number) =>
  http.get<AuditLog>(`/audit_logs/${id}`);
