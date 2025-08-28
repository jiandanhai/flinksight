import React, { useMemo, useState } from "react";
import { Layout, Menu, Spin } from "antd";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { MenuUnfoldOutlined, MenuFoldOutlined } from "@ant-design/icons";

import { useUser } from "@/context/UserContext";
import type { MenuProps } from "antd";
import type { MenuNodeDTO } from "@/api/dto";
import { useUserMenus } from "@/hooks/useUserMenus";

import { ClusterProvider, useCluster } from "@/context/ClusterContext";
import ClusterSwitcher from "@/components/ClusterSwitcher";

const { Header, Sider, Content } = Layout;
type ItemType = Required<MenuProps>["items"][number];

const InnerLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const { user } = useUser();
  const navigate = useNavigate();
  const location = useLocation();
  const { id: currentClusterId } = useCluster();

  const toggle = () => setCollapsed((v) => !v);

  // ⭐ 统一处理菜单
  const { data: menuTree, loading, error } = useUserMenus(user?.id, user?.tenantId);

  const toAntdItems = (nodes?: MenuNodeDTO[] | null): ItemType[] => {
    if (!nodes || nodes.length === 0) return [];
    return nodes.map((n) => {
      const key = n.path || n.key || String(n.id);
      return {
        key,
        label: n.title,
        children: n.children && n.children.length ? toAntdItems(n.children) : undefined,
      };
    });
  };

  const menuItems = useMemo(() => toAntdItems(menuTree), [menuTree]);
  const selectedKeys = useMemo(() => [location.pathname], [location.pathname]);

  const shouldShowSpinner = (!user || !user.id || !user.tenantId) || loading;
  if (shouldShowSpinner) return <Spin fullscreen tip="正在加载主界面…" />;
  if (error) console.error("❌ 获取菜单失败：", error);

  // ⬇️ 导航：/job 需要自动补 clusterId；没有则送去 /cluster
  const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
    const path = String(key);
    if (path === '/job') {
      if (currentClusterId && currentClusterId > 0) navigate(`/job?clusterId=${currentClusterId}`);
      else navigate('/cluster');
    } else {
      navigate(path);
    }
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider collapsible collapsed={collapsed} trigger={null} width={220}>
        <div className="logo" style={{ color: "white", fontSize: 18, padding: 16 }}>
          FlinkSight
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={selectedKeys}
          onClick={handleMenuClick}
          items={menuItems}
        />
      </Sider>

      <Layout>
        <Header style={{ background: "#fff", padding: "0 16px", display: 'flex', alignItems: 'center', gap: 12 }}>
          <span onClick={toggle} style={{ cursor: "pointer" }}>
            {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          </span>
          <div style={{ flex: 1 }} />
          {/* 右上角：集群切换器（写回 URL，持久化上下文） */}
          <ClusterSwitcher />
        </Header>
        <Content style={{ margin: 24, background: "#fff" }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

// 用 Provider 包一层，不用你改 App.tsx
const MainLayout: React.FC = () => (
  <ClusterProvider>
    <InnerLayout />
  </ClusterProvider>
);

export default MainLayout;
