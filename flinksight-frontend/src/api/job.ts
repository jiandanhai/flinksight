// /src/api/job.ts
import http from './http';
import type {
  Job,
  JobQuery,
  JobCreateReq,
  JobUpdateReq,
  JobDetail,
  JobActionReq,
  JobMetric,
  JobLog,
  JobDiagnosis,
  JobBatchOpReq,
} from '../types/job';

/** 查询任务列表（支持分页、条件筛选） */
export const getJobs = (params: JobQuery) =>
  http.get<Job[]>('/jobs', { params });

/** 查询单个任务详情 */
export const getJobDetail = (id: number) =>
  http.get<JobDetail>(`/jobs/${id}`);

/** 新建任务 */
export const createJob = (data: JobCreateReq) =>
  http.post<Job>('/jobs', data);

/** 编辑任务 */
export const updateJob = (id: number, data: JobUpdateReq) =>
  http.put<Job>(`/jobs/${id}`, data);

/** 删除任务（单个） */
export const deleteJob = (id: number) =>
  http.delete(`/jobs/${id}`);

/** 批量任务操作（批量启动/停止/重启/删除等） */
export const batchOperateJobs = (data: JobBatchOpReq) =>
  http.post('/jobs/batch-op', data);

/** 对单个任务执行操作（启动/停止/重启/暂停/恢复） */
export const operateJob = (id: number, data: JobActionReq) =>
  http.post(`/jobs/${id}/action`, data);

/** 查询任务指标（支持按时间范围、指标类型） */
export const getJobMetrics = (jobId: number, metricKey?: string, from?: string, to?: string) =>
  http.get<JobMetric[]>(`/jobs/${jobId}/metrics`, { params: { metricKey, from, to } });

/** 查询任务日志（支持按级别、时间段、分页） */
export const getJobLogs = (jobId: number, params?: { level?: string; from?: string; to?: string; page?: number; size?: number }) =>
  http.get<JobLog[]>(`/jobs/${jobId}/logs`, { params });

/** 任务一键诊断（自动分析健康度，生成诊断报告） */
export const diagnoseJob = (jobId: number) =>
  http.post<JobDiagnosis>(`/jobs/${jobId}/diagnose`, {});

/** 查询任务依赖链路（上下游） */
export const getJobDependencies = (jobId: number) =>
  http.get<{ dependencies: Array<{ id: number; name: string }> }>(`/jobs/${jobId}/dependencies`);

/** 导出任务配置或运行日志 */
export const exportJob = (jobId: number, type: 'config' | 'log') =>
  http.get<Blob>(`/jobs/${jobId}/export`, { params: { type }, responseType: 'blob' });

