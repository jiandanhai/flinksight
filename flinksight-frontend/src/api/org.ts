import http from './http';
import type { OrgNode } from '../types/org';

/**
 * 查询组织架构树
 */
export const getOrgTree = () =>
  http.get<OrgNode[]>('/org/tree');

/**
 * 新增组织节点
 */
export const createOrgNode = (data: Omit<OrgNode, 'id' | 'children'>) =>
  http.post('/org/node', data);

/**
 * 编辑组织节点
 */
export const updateOrgNode = (id: number, data: Partial<OrgNode>) =>
  http.put(`/org/node/${id}`, data);

/**
 * 删除组织节点
 */
export const deleteOrgNode = (id: number) =>
  http.delete(`/org/node/${id}`);
