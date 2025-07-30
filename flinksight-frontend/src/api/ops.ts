import http from './http';
import type { OpsTask, OpsTaskQuery } from '../types/ops';

/**
 * 查询运维任务列表
 */
export const getOpsTasks = (params: OpsTaskQuery) =>
  http.get<OpsTask[]>('/ops/tasks', { params });

/**
 * 新建运维任务
 */
export const createOpsTask = (data: Omit<OpsTask, 'id' | 'status' | 'result' | 'runBy' | 'runAt'>) =>
  http.post('/ops/tasks', data);

/**
 * 执行运维任务
 */
export const runOpsTask = (id: number) =>
  http.post(`/ops/tasks/${id}/run`, {});

/**
 * 删除运维任务
 */
export const deleteOpsTask = (id: number) =>
  http.delete(`/ops/tasks/${id}`);
