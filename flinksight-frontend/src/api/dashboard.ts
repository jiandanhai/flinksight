// /src/api/dashboard.ts
import http from './http';
import type {
  DashboardSummary,
  MetricTrend,
  FunnelData,
  HealthDistribution,
  AlertRealtimeItem,
  DashboardQuery,
} from '../types/dashboard';

/** 查询运营总览数据（核心KPI） */
export const getDashboardSummary = () =>
  http.get<DashboardSummary>('/dashboard/summary');

/** 查询业务指标趋势（如任务量、报警量、健康度等） */
export const getMetricTrends = (params: DashboardQuery) =>
  http.get<MetricTrend[]>('/dashboard/trends', { params });

/** 查询业务转化漏斗数据 */
export const getFunnelData = (params?: { type?: string; from?: string; to?: string }) =>
  http.get<FunnelData>('/dashboard/funnel', { params });

/** 查询当前健康分布 */
export const getHealthDistribution = () =>
  http.get<HealthDistribution>('/dashboard/health-distribution');

/** 查询实时报警监控（近N条） */
export const getRealtimeAlerts = (limit = 20) =>
  http.get<AlertRealtimeItem[]>('/dashboard/realtime-alerts', { params: { limit } });

/** 查询报警趋势（可用于大盘图表） */
export const getAlertTrends = (params: DashboardQuery) =>
  http.get<MetricTrend[]>('/dashboard/alert-trends', { params });

/** 查询活跃任务数、活跃报警、异常趋势等（用于首页小组件） */
export const getActiveWidgetStats = () =>
  http.get<{ activeJobs: number; todayAlerts: number; abnormalJobs: number }>('/dashboard/active-widget-stats');

