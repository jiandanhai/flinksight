/**
 * 运维脚本/任务实体
 */
export interface OpsTask {
  id: number;
  name: string;
  script: string;
  status: 'idle' | 'running' | 'success' | 'fail';
  result?: string;
  runBy: string;
  runAt: string;
}

/**
 * 运维任务查询参数
 */
export interface OpsTaskQuery {
  name?: string;
  status?: string;
  page?: number;
  size?: number;
}
