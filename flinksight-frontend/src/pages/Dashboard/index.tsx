// src/pages/dashboard/index.tsx
import React, { Suspense, useEffect, useState, useMemo } from "react";
import { Tabs } from "antd";
import type { TabsProps } from "antd";
import KPIStats from "./KPIStats";
import ClusterStatus from "./ClusterStatus";
import JobFunnel from "./JobFunnel";
import AlertTrend from "./AlertTrend";
import HealthDist from "./HealthDist";

// 使用 React.lazy 异步加载指标大屏
const MetricDashboard = React.lazy(() => import("./MetricDashboard"));

/**
 * Flinksight SaaS 仪表盘主页面
 * - 使用 antd Tabs 管理视图切换（业界标准、可访问性好）
 * - 支持运营概览与指标大屏两大视图
 * - Tab 状态可记忆，切换时动态更新 document.title
 */
const DashboardPage: React.FC = () => {
  const [activeKey, setActiveKey] = useState<"overview" | "metric">("overview");

  useEffect(() => {
    document.title =
      (activeKey === "overview" ? "运营总览" : "指标可视化大屏") +
      " - Flinksight";
  }, [activeKey]);

  useEffect(() => {
    console.debug("[DashboardPage] 页面已加载，当前tab:", activeKey);
  }, [activeKey]);

  const items: TabsProps["items"] = useMemo(
    () => [
      {
        key: "overview",
        label: "运营总览",
        children: (
          <>
            <KPIStats />
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
              <ClusterStatus />
              <HealthDist />
              <JobFunnel />
            </div>
            <div className="mt-8">
              <AlertTrend />
            </div>
          </>
        ),
      },
      {
        key: "metric",
        label: "指标可视化大屏",
        children: (
          <Suspense
            fallback={<div className="text-center py-32">指标大屏加载中...</div>}
          >
            <MetricDashboard />
          </Suspense>
        ),
      },
    ],
    []
  );

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <h1 className="font-bold text-2xl mb-6">
        {activeKey === "overview" ? "运营总览" : "指标可视化大屏"}
      </h1>
      <Tabs
        items={items}
        activeKey={activeKey}
        onChange={(k) => setActiveKey(k as "overview" | "metric")}
        size="large"
        tabBarGutter={32}
        animated
      />
    </div>
  );
};

export default DashboardPage;
