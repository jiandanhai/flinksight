/**
 * @file 运维与运营聚合页面
 * @desc 使用 antd Tabs 聚合视图
 *       1) SaaS运营大屏
 *       2) 运维自动化任务
 */
import React, { useEffect, useState, useMemo } from "react";
import { Tabs } from "antd";
import type { TabsProps } from "antd";
import OpsDashboard from "./OpsDashboard";
import OpsTaskPage from "./OpsTaskPage";

const OpsIndexPage: React.FC = () => {
  const [activeKey, setActiveKey] = useState<"dashboard" | "task">("dashboard");

  useEffect(() => {
    document.title =
      (activeKey === "dashboard" ? "SaaS运营大屏" : "运维自动化") +
      " - 运维与运营中心 - Flinksight";
  }, [activeKey]);

  const items: TabsProps["items"] = useMemo(
    () => [
      {
        key: "dashboard",
        label: "SaaS运营大屏",
        children: <OpsDashboard />,
      },
      {
        key: "task",
        label: "运维自动化",
        children: <OpsTaskPage />,
      },
    ],
    []
  );

  return (
    <div className="p-6 bg-gray-50 min-h-screen">
      <h2 className="font-bold text-2xl mb-6">运维与运营中心</h2>
      <Tabs
        items={items}
        activeKey={activeKey}
        onChange={(k) => setActiveKey(k as "dashboard" | "task")}
        size="large"
        tabBarGutter={32}
        animated
      />
    </div>
  );
};

export default OpsIndexPage;
