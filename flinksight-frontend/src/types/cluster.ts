// /src/types/cluster.ts

/**
 * 集群类型（YARN/K8S/Standalone）
 */
export type ClusterType = 'YARN' | 'K8S' | 'Standalone';

/**
 * 集群节点状态
 */
export type NodeStatus = 'online' | 'offline' | 'warning';

/**
 * 集群基础信息
 */
export interface Cluster {
  id: number;
  tenantId: number;
  name: string;
  type: ClusterType;
  endpoint: string;
  version?: string;
  tags?: string;
  status: 1 | 0; // 1正常，0禁用
  remark?: string;
  isDeleted?: 0 | 1;
  createTime: string;
}

/**
 * 新建/编辑集群请求
 */
export interface ClusterCreateReq {
  name: string;
  type: ClusterType;
  endpoint: string;
  version?: string;
  tags?: string;
  remark?: string;
}

/**
 * 编辑集群请求
 */
export type ClusterUpdateReq = Partial<ClusterCreateReq> & { id: number };

/**
 * 集群查询参数
 */
export interface ClusterQuery {
  name?: string;
  type?: ClusterType;
  status?: 1 | 0;
  page?: number;
  size?: number;
}

/**
 * 集群节点明细
 */
export interface ClusterNode {
  id: number;
  clusterId: number;
  name: string;
  ip: string;
  status: NodeStatus;
  cpuUsage: number;
  memUsage: number;
  roles: string[];
  version?: string;
  remark?: string;
  createTime: string;
}

/**
 * 集群节点查询参数
 */
export interface NodeQuery {
  clusterId: number;
  name?: string;
  status?: NodeStatus;
  page?: number;
  size?: number;
}

/**
 * 集群节点监控指标
 */
export interface NodeMetric {
  nodeId: number;
  cpu: number;
  mem: number;
  ts: string;
}

/**
 * 集群指标趋势
 */
export interface ClusterMetric {
  clusterId: number;
  metricKey: string; // 如 'cpu', 'mem', 'jobs', 'health'
  value: number;
  ts: string;
}

/**
 * 集群运维操作参数
 */
export interface ClusterOpReq {
  id: number;
  action: 'expand' | 'shrink' | 'restart' | 'pause';
  params?: Record<string, any>;
}

/** 节点创建请求 */
export interface NodeCreateReq {
  name: string;
  ip: string;
  role: string;
  clusterName: string;
  enabled?: boolean;
}

/** 节点批量扩容返回 */
export interface NodeBatchAddResult {
  success: number;     // 成功数量
  fail: number;        // 失败数量
  failList?: { name: string; reason: string }[]; // 失败明细
}

/** 节点结构 */
export interface Node {
  id: number;
  name: string;
  ip: string;
  role: string;
  clusterName: string;
  enabled: boolean;
  health: string;
  cpuUsage: number;
  memUsage: number;
  // ...其它
}

/** 节点查询条件 */
export interface NodeQuery {
  page?: number;
  size?: number;
  keyword?: string;
}