import React, { useEffect, useState } from "react";
import { Layout, Menu, Spin } from "antd";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { MenuUnfoldOutlined, MenuFoldOutlined } from "@ant-design/icons";

import { getMenusByPermission } from "../utils/menu";
import api from "../api/gen/client";
import { useUser } from "../context/UserContext"; // ✅ 新增：引入 useUser

const { Header, Sider, Content } = Layout;

const MainLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const [menuItems, setMenuItems] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const location = useLocation();

  const { user } = useUser(); // ✅ 全局 user 状态

  const toggle = () => setCollapsed(!collapsed);

  const fetchUserPermissions = async (userId: number, tenantId: number) => {
    try {
      const res = await api.userPermissions({ userId, tenantId });
      const permissions = res.data?.permissions || [];
      const menus = getMenusByPermission(permissions);
      setMenuItems(menus);
    } catch (e) {
      console.error("❌ 获取用户权限失败:", e);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // ✅ 用 useUser() 替代 localStorage.getItem("user")
    if (user?.id && user?.tenantId) {
      fetchUserPermissions(user.id, user.tenantId);
    } else {
      console.warn("⚠️ 未找到用户信息，跳过权限加载");
      setLoading(false);
    }
  }, [user]);

  if (loading) return <Spin fullscreen />;

  return (
    <Layout style={{ minHeight: "100vh" }}>
      <Sider collapsible collapsed={collapsed} trigger={null} width={220}>
        <div className="logo" style={{ color: "white", fontSize: 18, padding: 16 }}>
          FlinkSight
        </div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          onClick={({ key }) => navigate(key)}
          items={menuItems}
        />
      </Sider>
      <Layout>
        <Header style={{ background: "#fff", padding: 0 }}>
          <span onClick={toggle} style={{ marginLeft: 16 }}>
            {collapsed ? <MenuUnfoldOutlined /> : <MenuFoldOutlined />}
          </span>
        </Header>
        <Content style={{ margin: 24, background: "#fff" }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
};

export default MainLayout;
