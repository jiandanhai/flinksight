/**
 * 状态常量与枚举
 */

/**
 * @file 通用常量
 * @desc 枚举、字典、API路径集中管理
 */
export const USER_STATUS = {
  ENABLED: 1,
  DISABLED: 0,
};

export const API_PATHS = {
  USER: '/users',
  ROLE: '/roles',
  CLUSTER: '/clusters',
  ALERT: '/alerts',
  JOB: '/jobs',
};

export const CLUSTER_STATUS = {
  0: '未知',
  1: '健康',
  2: '警告',
  3: '故障',
};

export const JOB_STATUS = {
  0: '等待',
  1: '运行中',
  2: '已完成',
  3: '失败',
  4: '取消',
};

export const ALERT_LEVEL = {
  1: '信息',
  2: '警告',
  3: '严重',
  4: '致命',
};
