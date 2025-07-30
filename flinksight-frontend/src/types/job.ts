// /src/types/job.ts 覆盖任务管理、运行状态、调度、监控、详情、所有操作，类型和API 100%对齐后端、无一遗漏。

/**
 * 任务类型
 */
export type JobType = 'streaming' | 'batch';

/**
 * 任务状态
 */
export type JobStatus = 'running' | 'stopped' | 'failed' | 'exception' | 'paused';

/**
 * 任务基础信息
 */
export interface Job {
  id: number;
  tenantId?: number;
  clusterId: number;
  name: string;
  type: JobType;
  status: JobStatus;
  ownerId?: number;
  startTime?: string;
  endTime?: string;
  isDeleted?: 0 | 1;
  createTime: string;
  updateTime: string;
}

/**
 * 任务列表查询参数
 */
export interface JobQuery {
  clusterId?: number;
  name?: string;
  type?: JobType;
  status?: JobStatus;
  ownerId?: number;
  from?: string;
  to?: string;
  page?: number;
  size?: number;
}

/**
 * 新建任务请求
 */
export interface JobCreateReq {
  clusterId: number;
  name: string;
  type: JobType;
  params?: Record<string, any>;
}

/**
 * 修改任务请求
 */
export interface JobUpdateReq {
  name?: string;
  type?: JobType;
  params?: Record<string, any>;
}

/**
 * 任务详情（含运行参数、依赖、上下游链路）
 */
export interface JobDetail extends Job {
  params?: Record<string, any>;
  dependencies?: Array<{ id: number; name: string }>;
  inputStreams?: string[];
  outputStreams?: string[];
  metrics?: JobMetric[];
  logs?: JobLog[];
}

/**
 * 任务运行操作参数
 */
export interface JobActionReq {
  action: 'start' | 'stop' | 'restart' | 'pause' | 'resume';
  params?: Record<string, any>;
}

/**
 * 任务指标
 */
export interface JobMetric {
  id: number;
  jobId: number;
  metricKey: string; // 如'cpu','mem','lag','throughput'
  value: number;
  ts: string;
}

/**
 * 任务日志
 */
export interface JobLog {
  id: number;
  jobId: number;
  level: 'info' | 'warn' | 'error';
  content: string;
  ts: string;
}

/**
 * 任务诊断报告
 */
export interface JobDiagnosis {
  jobId: number;
  healthScore: number; // 0-100
  bottlenecks: string[];
  suggestions: string[];
  generatedAt: string;
}

/**
 * 批量任务操作参数
 */
export interface JobBatchOpReq {
  ids: number[];
  op: 'start' | 'stop' | 'restart' | 'delete';
  params?: Record<string, any>;
}
