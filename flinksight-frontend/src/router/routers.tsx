/**
 * @file 路由菜单配置（含权限与多级页面结构）——基于 index.tsx 作为入口
 * - 父级都挂各模块 index.tsx
 * - 兼容旧的 /xxx/list 等路径，重定向到父级 /xxx
 * - 详情页由各 index.tsx 内部用 state 切换（不再配 /:id）
 */

import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import {
  AreaChartOutlined,
  BellOutlined,
  CloudOutlined,
  DatabaseOutlined,
  FundOutlined,
  HomeOutlined,
  SettingOutlined,
  UserOutlined,
} from '@ant-design/icons';

// ====== 各模块 index.tsx 作为入口 ======
import Dashboard from '../pages/Dashboard';          // /dashboard → Dashboard/index.tsx

import ClusterIndex from '../pages/Cluster';         // /cluster   → Cluster/index.tsx
import JobIndex from '../pages/Job';                 // /job       → Job/index.tsx
import AlertIndex from '../pages/Alert';             // /alerts    → Alert/index.tsx
import MetricIndex from '../pages/Metric';           // /metrics   → Metric/index.tsx（Tab：指标管理/指标大屏）
import OpsIndex from '../pages/Ops';                 // /ops       → Ops/index.tsx

import FileManager from '../pages/File/FileManager';

// 个人中心
import ProfileIndex from '../pages/Profile';         // /profile   → Profile/index.tsx

// 系统设置（基于 index.tsx）
import SettingsUser from '../pages/User';            // /settings/user   → User/index.tsx
import SettingsRole from '../pages/Role';            // /settings/role   → Role/index.tsx
import SettingsTenant from '../pages/Tenant';        // /settings/tenant → Tenant/index.tsx
import SettingsBasic from '../pages/Settings';       // /settings/basic  → Setting/index.tsx

// 仍保留的独立页面
import NotificationCenter from '../pages/Notification/NotificationCenter';

// ===== types =====
export interface AppRoute {
  path: string;
  title: string;
  icon?: React.ReactNode;
  component?: React.FC;
  perm?: string;                // 与后端 permission.code 对齐
  hideInMenu?: boolean;         // 不在菜单显示（如详情页等）
  children?: AppRoute[];
  redirect?: string;            // 父级访问时的重定向

  /** 可选：强制该节点在菜单里显示子项；默认自动规则（仅当子项>=2时显示） */
  showChildrenInMenu?: boolean;
}

// ===== route tree =====
const routes: AppRoute[] = [
  // 顶层：总览
  { path: '/dashboard', title: '总览大屏', icon: <HomeOutlined />, component: Dashboard, perm: 'DASHBOARD_VIEW' },

  // 集群管理：父级进 index.tsx；旧 /cluster/list → /cluster
  {
    path: '/cluster',
    title: '集群管理',
    icon: <DatabaseOutlined />,
    perm: 'CLUSTER_VIEW',
    component: ClusterIndex,
    children: [
      { path: '/cluster/list', title: '集群列表（旧链路）', redirect: '/cluster', hideInMenu: true },
    ],
  },

  // 作业运维：父级进 index.tsx；旧 /job/list → /job
  {
    path: '/job',
    title: '作业运维',
    icon: <DatabaseOutlined />,
    perm: 'JOB_VIEW',
    component: JobIndex,
    children: [
      { path: '/job/list', title: '作业列表（旧链路）', redirect: '/job', hideInMenu: true },
    ],
  },

  // 报警中心：父级只做占位 + 重定向到 /alerts/list；子路由各自渲染
  {
    path: '/alerts',
    title: '报警中心',
    icon: <BellOutlined />,
    perm: 'ALERT_VIEW',
    redirect: '/alerts/list',            // 访问 /alerts 时重定向到列表页
    showChildrenInMenu: true,            // 菜单里展示“通知中心”等子项
    children: [
      { path: '/alerts/list',          title: '报警流水',   component: AlertIndex,          perm: 'ALERT_VIEW' },
      { path: '/alerts/notification',  title: '通知中心',   component: NotificationCenter,  perm: 'ALERT_VIEW' },
    ],
  },
  
  // 指标可视化：父级进入 index.tsx（内部 Tabs：指标管理/指标大屏）
  {path: '/metrics',title: '指标可视化',icon: <HomeOutlined />,component: MetricIndex,perm: 'METRICS_VIEW'},

  // 运营大屏：父级进 index.tsx；旧 /ops/dashboard → /ops
  {
    path: '/ops',
    title: '运营大屏',
    icon: <FundOutlined />,
    perm: 'OPS_VIEW',
    component: OpsIndex,
    children: [
      { path: '/ops/dashboard', title: 'SaaS运营大屏（旧链路）', redirect: '/ops', hideInMenu: true },
    ],
  },

  // 文件中心
  { path: '/files', title: '文件中心', icon: <CloudOutlined />, component: FileManager, perm: 'FILE_VIEW' },

  // 个人中心：父级进 index.tsx；旧 /profile/center → /profile
  {
    path: '/profile',
    title: '个人中心',
    icon: <UserOutlined />,
    perm: 'USER_VIEW',
    component: ProfileIndex,
    children: [
      { path: '/profile/center', title: '我的资料（旧链路）', redirect: '/profile', hideInMenu: true },
    ],
  },

  // 系统设置：父级重定向到 /settings/user；子路由均挂各自 index.tsx
  {
    path: '/settings',
    title: '系统设置',
    icon: <SettingOutlined />,
    perm: 'SETTINGS_VIEW',
    redirect: '/settings/user',
    children: [
      { path: '/settings/user',   title: '用户管理', component: SettingsUser,   perm: 'USER_VIEW' },
      { path: '/settings/role',   title: '角色管理', component: SettingsRole,   perm: 'ROLE_VIEW' },
      { path: '/settings/tenant', title: '租户管理', component: SettingsTenant, perm: 'TENANT_VIEW' },
      { path: '/settings/basic',  title: '基础设置', component: SettingsBasic,  perm: 'SETTINGS_VIEW' },
    ],
  },
];

export default routes;

// ================== 工具函数 ==================

/** 依据权限集合过滤路由（前端鉴权渲染菜单/路由） */
export function filterRoutesByPerm(all: AppRoute[], permSet: Set<string>): AppRoute[] {
  const walk = (nodes: AppRoute[]): AppRoute[] =>
    nodes
      .filter((r) => !r.perm || permSet.has(r.perm))
      .map((r) => {
        const children = r.children ? walk(r.children) : undefined;
        // 父级既没组件也没子路由也没 redirect 就剔除
        if (!r.component && (!children || children.length === 0) && !r.redirect) return null as unknown as AppRoute;
        return { ...r, children };
      })
      .filter(Boolean) as AppRoute[];
  return walk(all);
}

/** 生成 React Router v6 路由结构（父级无组件但有子路由时自动补 <Outlet/>） */
export function toReactRouter(all: AppRoute[]) {
  const rel = (p: string) => (p.startsWith('/') ? p.slice(1) : p);
  const childPath = (parent: string, child: string) =>
    rel(child).startsWith(rel(parent)) ? rel(child).slice(rel(parent).length).replace(/^\//, '') : rel(child);

  const build = (nodes: AppRoute[], parentPath = ''): any[] =>
    nodes.map((r) => {
      const node: any = {};
      node.path = parentPath ? childPath(parentPath, r.path) : rel(r.path);

      if (!r.component && r.children && r.children.length) {
        node.element = <Outlet />; // 父级只有子路由 → 使用 Outlet 占位
      } else if (r.redirect && !r.component) {
        node.element = <Navigate to={rel(r.redirect)} replace />; // 绝对/相对均可
      } else if (r.component) {
        node.element = React.createElement(r.component);
      }

      if (r.children && r.children.length) {
        node.children = build(r.children, r.path);
        // 父级 index 重定向（如 /settings → /settings/user）
        if (r.redirect) {
          const to = r.redirect.replace(r.path, '').replace(/^\//, '');
          node.children.unshift({ index: true, element: <Navigate to={to} replace /> });
        }
      }
      return node;
    });

  return build(all);
}

/**
 * 生成 antd Menu items：
 * - 默认：仅当“可见子路由 >= 2”时才显示二级菜单；
 * - 可通过 r.showChildrenInMenu 强制显示；
 * - hideInMenu=true 的路由不会展示。
 * - ✅ 这条规则能把 /metrics 下“只有一个子项”的情况彻底隐藏掉。
 */
export function toMenuItems(all: AppRoute[]) {
  const visibleChildren = (r: AppRoute) => (r.children || []).filter((c) => !c.hideInMenu);

  const mapNode = (r: AppRoute) => {
    const kids = visibleChildren(r);
    const showKids = r.showChildrenInMenu ?? (kids.length >= 2);

    return {
      key: r.path,
      icon: r.icon,
      label: r.title,
      children: showKids ? kids.map(mapNode) : undefined,
    };
  };

  return all.filter((r) => !r.hideInMenu).map(mapNode);
}
