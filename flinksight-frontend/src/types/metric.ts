/**
 * 指标实体
 */
export interface Metric {
  id: number;
  name: string;
  code: string;
  desc?: string;
  type: string; // 指标类型
  unit: string; // 单位
  tags?: string;
  createTime: string;
}

/**
 * 指标查询参数
 */
export interface MetricQuery {
  keyword?: string;
  type?: string;
  page?: number;
  size?: number;
}

/**
 * 创建指标参数
 */
export interface MetricCreateReq {
  name: string;
  code: string;
  desc?: string;
  type: string;
  unit: string;
  tags?: string;
}

/**
 * 更新指标参数
 */
export interface MetricUpdateReq {
  name?: string;
  code?: string;
  desc?: string;
  type?: string;
  unit?: string;
  tags?: string;
}
