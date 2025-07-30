/**
 * 组织架构节点
 */
export interface OrgNode {
  id: number;
  name: string;
  parentId: number | null;
  children?: OrgNode[];
  leader?: string;
  type: 'company' | 'department' | 'team';
}
