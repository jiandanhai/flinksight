/**
 * @file 路由菜单配置（含权限与多级页面结构）
 */
import {
    AreaChartOutlined,
    BellOutlined,
    CloudOutlined,
    DatabaseOutlined,
    FundOutlined,
    HomeOutlined,
    SettingOutlined,
    UserOutlined
} from '@ant-design/icons';
import Dashboard from "../pages/Dashboard";
import TenantList from "../pages/Tenant/TenantList";
import UserList from "../pages/User/UserList";
import AlertList from "../pages/Alert/AlertList";
import ClusterList from "../pages/Cluster/ClusterList";
import JobList from "../pages/Job/JobList";
import NotificationCenter from "../pages/Notification/NotificationCenter";
import SystemSettings from "../pages/System/SystemSettings";
import FileManager from "../pages/File/FileManager";
import Profile from "../pages/User/Profile";
import JobDetail from "../pages/Job/JobDetail";
import NodeStatus from "../pages/Cluster/NodeStatus";
import MetricDashboard from "../pages/Metric/MetricDashboard";
import OpsDashboard from "../pages/Ops/OpsDashboard";

export interface RouteMeta {
  path: string;
  title: string;
  icon?: React.ReactNode;
  component?: React.FC;
  perm?: string;
  children?: RouteMeta[];
}

const routes: RouteMeta[] = [
  { path: "/dashboard", title: "总览大屏", icon: <HomeOutlined />, component: Dashboard },
  {
    path: "/tenant", title: "租户管理", icon: <CloudOutlined />, children: [
      { path: "/tenant/list", title: "租户列表", component: TenantList },
    ]
  },
  {
    path: "/users", title: "用户中心", icon: <UserOutlined />, children: [
      { path: "/users/list", title: "用户列表", component: UserList },
      { path: "/users/profile", title: "个人中心", component: Profile }
    ]
  },
  {
    path: "/alerts", title: "报警中心", icon: <BellOutlined />, children: [
      { path: "/alerts/list", title: "报警流水", component: AlertList },
      { path: "/alerts/notification", title: "通知中心", component: NotificationCenter }
    ]
  },
  {
    path: "/cluster", title: "集群与任务", icon: <DatabaseOutlined />, children: [
      { path: "/cluster/list", title: "集群管理", component: ClusterList },
      { path: "/job/list", title: "作业列表", component: JobList }
    ]
  },
  { path: "/files", title: "文件中心", icon: <CloudOutlined />, component: FileManager },
  { path: "/settings", title: "系统设置", icon: <SettingOutlined />, component: SystemSettings },
    {
    path: "/job", title: "作业运维", icon: <DatabaseOutlined />, children: [
      { path: "/job/list", title: "作业列表", component: JobList },
      { path: "/job/detail/:jobId", title: "作业明细", component: JobDetail, perm: "job:view" }
    ]
  },
  {
    path: "/cluster", title: "集群管理", icon: <CloudOutlined />, children: [
      { path: "/cluster/list", title: "集群列表", component: ClusterList },
      { path: "/cluster/node", title: "节点状态", component: NodeStatus }
    ]
  },
  {
    path: "/metrics", title: "指标可视化", icon: <AreaChartOutlined />, children: [
      { path: "/metrics/dashboard", title: "指标大屏", component: MetricDashboard }
    ]
  },
  {
    path: "/ops", title: "运营大屏", icon: <FundOutlined />, children: [
      { path: "/ops/dashboard", title: "SaaS运营大屏", component: OpsDashboard }
    ]
  }
];

export default routes;
