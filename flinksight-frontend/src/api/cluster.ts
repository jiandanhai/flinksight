// /src/api/cluster.ts
import http from './http';
import type {
  Cluster,
  ClusterQuery,
  ClusterCreateReq,
  ClusterUpdateReq,
  ClusterNode,
  NodeQuery,
  NodeMetric,
  ClusterMetric,
  ClusterOpReq,
} from '../types/cluster';

/** 查询集群列表（分页/条件） */
export const getClusters = (params: ClusterQuery) =>
  http.get<Cluster[]>('/clusters', { params });

/** 查询单个集群详情 */
export const getClusterDetail = (id: number) =>
  http.get<Cluster>(`/clusters/${id}`);

/** 新建集群 */
export const createCluster = (data: ClusterCreateReq) =>
  http.post<Cluster>('/clusters', data);

/** 编辑集群 */
export const updateCluster = (id: number, data: ClusterUpdateReq) =>
  http.put<Cluster>(`/clusters/${id}`, data);

/** 删除集群（物理/软删，后端实现为准） */
export const deleteCluster = (id: number) =>
  http.delete(`/clusters/${id}`);

/** 批量删除集群 */
export const deleteClusters = (ids: number[]) =>
  http.post('/clusters/batch-delete', { ids });

/** 集群一键接入（引导/自动发现） */
export const connectCluster = (data: ClusterCreateReq) =>
  http.post<Cluster>('/clusters/connect', data);

/** 查询集群健康状态&全局概览 */
export const getClusterOverview = (id: number) =>
  http.get(`/clusters/${id}/overview`);

/** 查询集群节点列表 */
export const getClusterNodes = (params: NodeQuery) =>
  http.get<ClusterNode[]>('/nodes', { params });

/** 查询节点详情 */
export const getNodeDetail = (id: number) =>
  http.get<ClusterNode>(`/nodes/${id}`);

/** 节点监控指标（按时间） */
export const getNodeMetrics = (nodeId: number, from: string, to: string) =>
  http.get<NodeMetric[]>(`/nodes/${nodeId}/metrics`, { params: { from, to } });

/** 集群监控指标趋势（如cpu/mem/健康度等） */
export const getClusterMetrics = (
  clusterId: number,
  metricKey: string,
  from: string,
  to: string,
) =>
  http.get<ClusterMetric[]>(`/clusters/${clusterId}/metrics`, {
    params: { metricKey, from, to },
  });

/** 集群节点运维操作（如扩容/重启/暂停） */
export const operateCluster = (data: ClusterOpReq) =>
  http.post(`/clusters/${data.id}/ops`, data);

/** 新增/编辑集群节点（仅限人工维护场景） */
export const createNode = (data: Omit<ClusterNode, 'id' | 'createTime'>) =>
  http.post<ClusterNode>('/nodes', data);

export const updateNode = (id: number, data: Partial<ClusterNode>) =>
  http.put<ClusterNode>(`/nodes/${id}`, data);

export const deleteNode = (id: number) =>
  http.delete(`/nodes/${id}`);

/** 节点批量操作（如批量移除/扩容/维护） */
export const batchNodeOp = (ids: number[], op: string, params?: any) =>
  http.post('/nodes/batch-op', { ids, op, params });

/** 新增单节点 */
export const addNode = (data: NodeCreateReq) => http.post('/node', data);

/** 批量扩容（返回成功/失败数） */
export const batchAddNodes = (nodes: NodeCreateReq[]) =>
  http.post<NodeBatchAddResult>('/node/batch-add', { nodes });

/** 查询节点（带分页/过滤） */
export const getNodes = (params: NodeQuery) => http.get<{ records: Node[], total: number }>('/node', { params });

/** 节点健康统计 */
export const getNodeHealthStats = (clusterId: number) =>
  http.get<{ healthy: number; warning: number; error: number }>(`/cluster/${clusterId}/node/health`);
