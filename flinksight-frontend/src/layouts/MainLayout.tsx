// src/layouts/MainLayout.tsx
import React, { useMemo, useState } from "react";
import { Layout, Menu, Spin } from "antd";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { MenuUnfoldOutlined, MenuFoldOutlined } from "@ant-design/icons";

import { useUser } from "@/context/UserContext";
import type { MenuProps } from "antd";
import type { MenuNodeDTO } from "@/api/dto";
import { useUserMenus } from "@/hooks/useUserMenus";

const { Header, Sider, Content } = Layout;
type ItemType = Required<MenuProps>["items"][number];

const MainLayout: React.FC = () => {
  const [collapsed, setCollapsed] = useState(false);
  const { user } = useUser();
  const navigate = useNavigate();
  const location = useLocation();

  const toggle = () => setCollapsed((v) => !v);

  console.log("🎯 MainLayout 挂载");
  console.log("👤 当前 user：", user);

  // ⭐ 最优：用 Hook 统一处理 StrictMode 双执行、缓存、loading、异常
  const { data: menuTree, loading, error } = useUserMenus(user?.id, user?.tenantId);

  const toAntdItems = (nodes?: MenuNodeDTO[] | null): ItemType[] => {
    if (!nodes || nodes.length === 0) return [];
    return nodes.map((n) => {
      const key = n.path || n.key || String(n.id); // path 优先，其次 key，兜底 id
      return {
        key,
        label: n.title,
        // 需要 icon 时，可在这里把后端 icon 名 -> 组件 做映射
        children: n.children && n.children.length ? toAntdItems(n.children) : undefined,
      };
    });
  };

  const menuItems = useMemo(() => {
    const items = toAntdItems(menuTree);
    console.log("🧩 转换为 antd items：", items);
    if (!items.length && !loading && !error) {
      console.warn("⚠️ 菜单为空，请检查后端 required_code 与用户权限是否匹配。");
    }
    return items;
  }, [menuTree, loading, error]);

  const selectedKeys = useMemo(() => [location.pathname], [location.pathname]);

  // 没有 user 信息时不去拉接口；Hook 已处理 loading=false，不会卡死
  const shouldShowSpinner = (!user || !user.id || !user.tenantId) || loading;

  if (shouldShowSpinner) {
    console.log("⏳ loading 中…");
    return <Spin fullscreen tip="正在加载主界面…" />;
  }

  if (error) {
    console.error("❌ 获取菜单失败：", error);
  }

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
          onClick={({ key }) => navigate(String(key))}
          items={menuItems}
        />
      </Sider>

      <Layout>
        <Header style={{ background: "#fff", padding: 0 }}>
          <span onClick={toggle} style={{ marginLeft: 16, cursor: "pointer" }}>
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
