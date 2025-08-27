/**
 * @file 用户与权限管理页面
 * @desc 使用 antd Tabs 聚合
 *       1) 用户列表
 *       2) 角色管理
 *       3) 个人资料
 */
import React, { useEffect, useState, useMemo } from "react";
import { Tabs } from "antd";
import type { TabsProps } from "antd";
import UserList from "../User/UserList";
import RoleList from "../Role/RoleList";
import Profile from "../Profile/ProfileCenter";

const UserPage: React.FC = () => {
  const [activeKey, setActiveKey] = useState<"user" | "role" | "profile">("user");

  useEffect(() => {
    let title = "用户与权限管理 - Flinksight";
    if (activeKey === "role") title = "角色管理 - Flinksight";
    if (activeKey === "profile") title = "个人资料 - Flinksight";
    document.title = title;
  }, [activeKey]);

  const items: TabsProps["items"] = useMemo(
    () => [
      {
        key: "user",
        label: "用户列表",
        children: <UserList />,
      },
      {
        key: "role",
        label: "角色管理",
        children: <RoleList />,
      },
      {
        key: "profile",
        label: "个人资料",
        children: <Profile />,
      },
    ],
    []
  );

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="font-bold text-xl mb-6">用户与权限管理</h2>
      <Tabs
        items={items}
        activeKey={activeKey}
        onChange={(k) => setActiveKey(k as "user" | "role" | "profile")}
        size="large"
        tabBarGutter={32}
        animated
      />
    </div>
  );
};

export default UserPage;
