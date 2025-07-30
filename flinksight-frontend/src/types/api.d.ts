// 报警主类型
export interface Alert {
  id: number;
  tenantId: number;
  message: string;
  level: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  status: 'OPEN' | 'CLOSED' | 'ACKED';
  timestamp: string;  // ISO时间
  createdBy: number;
  updatedAt: string;
}

// 创建/更新Payload
export interface CreateAlertPayload {
  tenantId: number;
  message: string;
  level: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
}
export interface UpdateAlertPayload {
  message?: string;
  level?: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  status?: 'OPEN' | 'CLOSED' | 'ACKED';
}

// 列表查询参数
export interface AlertQueryParams {
  tenantId?: number;
  status?: string;
  level?: string;
  page: number;
  pageSize: number;
}
