import React from "react";
import { useSearchParams } from "react-router-dom";
import ClusterList from "./ClusterList";
import ClusterDetail from "./ClusterDetail";

/**
 * 集群管理主入口：
 * - 默认显示列表
 * - URL 出现 ?detail=xxx 时显示详情；Tab 用 ?tab=nodes|status
 */
const ClusterPage: React.FC = () => {
  const [sp, setSp] = useSearchParams();
  const detail = sp.get("detail");
  const id = detail ? Number(detail) : NaN;

  if (Number.isFinite(id)) {
    return <ClusterDetail />; // 详情页内部自己从 ?detail 读取
  }

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">集群管理</h2>
      <ClusterList
        onOpenDetail={(clusterId, tab) => {
          const next = new URLSearchParams(sp);
          next.set("detail", String(clusterId));
          if (tab) next.set("tab", tab);
          setSp(next, { replace: false });
        }}
      />
    </div>
  );
};

export default ClusterPage;
