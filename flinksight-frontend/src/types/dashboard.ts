// /src/types/dashboard.ts Dashboard监控与指标相关类型，100%覆盖后端接口，便于可视化

/**
 * 总览统计指标
 */
export interface DashboardSummary {
  totalClusters: number;
  totalJobs: number;
  totalAlerts: number;
  activeJobs: number;
  todayAlerts: number;
  healthyRate: number;   // 健康度百分比
  abnormalJobs: number;
  abnormalClusters: number;
  lastUpdate: string;
}

/**
 * 业务指标趋势（如任务量、报警量、健康度、吞吐量等按天/小时）
 */
export interface MetricTrendPoint {
  ts: string;   // 时间点
  value: number;
}

/**
 * 业务指标趋势数据
 */
export interface MetricTrend {
  metricKey: string;              // 业务指标名
  data: MetricTrendPoint[];       // 趋势点
}

/**
 * 漏斗分析数据（如业务转化漏斗）
 */
export interface FunnelStage {
  name: string;
  value: number;
  percent: number; // 百分比
}

export interface FunnelData {
  stages: FunnelStage[];
}

/**
 * 健康分布（当前各任务/集群健康分布统计）
 */
export interface HealthDistribution {
  healthy: number;
  warning: number;
  critical: number;
}

/**
 * 实时报警监控项
 */
export interface AlertRealtimeItem {
  id: number;
  jobId: number;
  clusterId: number;
  level: string;
  type: string;
  message: string;
  status: number;
  createTime: string;
}

/**
 * 大盘业务指标/报警趋势查询参数
 */
export interface DashboardQuery {
  from?: string;
  to?: string;
  metricKey?: string;
  groupBy?: 'hour' | 'day' | 'week';
}
