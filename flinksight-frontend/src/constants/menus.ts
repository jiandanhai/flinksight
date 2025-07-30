/**
 * @file 菜单声明文件（多级、国际化、微前端、权限全自动）
 */
import { ReactNode } from 'react';

// 通用菜单类型
export interface MenuItem {
  key: string;
  path: string;
  title: string | ((locale: string) => string); // 支持国际化函数
  icon?: ReactNode;
  roles?: string[]; // 允许访问的角色
  element?: string; // 本地页面组件名
  microApp?: string; // 微前端app名（如 qiankun、module federation接入）
  children?: MenuItem[];
}

// 国际化菜单标题
const I18N = {
  dashboard: { zh: '总览', en: 'Dashboard' },
  cluster: { zh: '集群', en: 'Clusters' },
  node: { zh: '节点', en: 'Nodes' },
  job: { zh: '任务', en: 'Jobs' },
  alert: { zh: '报警', en: 'Alerts' },
  user: { zh: '用户', en: 'Users' },
  role: { zh: '角色', en: 'Roles' },
  tenant: { zh: '租户', en: 'Tenant' },
  system: { zh: '系统', en: 'System' },
  ops: { zh: '运维', en: 'Operation' },
  mfa: { zh: '子系统', en: 'SubSystem' },
  microapp: { zh: '外部应用', en: 'MicroApp' },
};

export const MENUS: MenuItem[] = [
  {
    key: 'dashboard',
    path: '/dashboard',
    title: (locale: string) => I18N.dashboard[locale],
    roles: ['admin', 'ops', 'user'],
    element: 'DashboardPage',
  },
  {
    key: 'cluster',
    path: '/cluster',
    title: (locale: string) => I18N.cluster[locale],
    icon: null,
    roles: ['admin', 'ops'],
    element: 'ClusterPage',
    children: [
      {
        key: 'node',
        path: '/cluster/node',
        title: (locale: string) => I18N.node[locale],
        element: 'NodePage',
        roles: ['admin', 'ops'],
      }
    ]
  },
  {
    key: 'job',
    path: '/job',
    title: (locale: string) => I18N.job[locale],
    roles: ['admin', 'ops', 'user'],
    element: 'JobPage'
  },
  {
    key: 'alert',
    path: '/alert',
    title: (locale: string) => I18N.alert[locale],
    roles: ['admin', 'ops'],
    element: 'AlertPage',
    children: [
      {
        key: 'rule',
        path: '/alert/rule',
        title: '规则管理',
        element: 'RulePage',
        roles: ['admin', 'ops'],
      }
    ]
  },
  {
    key: 'system',
    path: '/settings',
    title: (locale: string) => I18N.system[locale],
    roles: ['admin'],
    element: 'SettingsPage',
    children: [
      {
        key: 'user',
        path: '/settings/user',
        title: (locale: string) => I18N.user[locale],
        element: 'UserPage',
        roles: ['admin']
      },
      {
        key: 'role',
        path: '/settings/role',
        title: (locale: string) => I18N.role[locale],
        element: 'RolePage',
        roles: ['admin']
      },
      {
        key: 'tenant',
        path: '/settings/tenant',
        title: (locale: string) => I18N.tenant[locale],
        element: 'TenantPage',
        roles: ['admin']
      }
    ]
  },
  {
    key: 'ops',
    path: '/ops',
    title: (locale: string) => I18N.ops[locale],
    roles: ['admin', 'ops'],
    element: 'OpsPage',
  },
  // 微前端/外部系统
  {
    key: 'mfa',
    path: '/mfa',
    title: (locale: string) => I18N.mfa[locale],
    microApp: 'mfa-app', // 微前端子应用名
    roles: ['admin', 'ops'],
  },
  {
    key: 'microapp',
    path: '/microapp',
    title: (locale: string) => I18N.microapp[locale],
    microApp: 'external-app',
    roles: ['admin', 'ops', 'user'],
  },
];
