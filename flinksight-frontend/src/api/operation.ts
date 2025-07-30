import http from './http';
import type { OperationLog, OperationLogQuery } from '../types/operation';

/**
 * 查询操作日志列表
 */
export const getOperationLogs = (params: OperationLogQuery) =>
  http.get<OperationLog[]>('/operation/logs', { params });

/**
 * 删除单条操作日志
 */
export const deleteOperationLog = (id: number) =>
  http.delete(`/operation/logs/${id}`);

/**
 * 批量删除操作日志
 */
export const deleteOperationLogs = (ids: number[]) =>
  http.post('/operation/logs/batch-delete', { ids });
