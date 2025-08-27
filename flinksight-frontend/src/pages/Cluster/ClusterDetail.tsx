// 只展示关键改动，未动你原有接口与布局
import React, { useEffect, useState } from "react";
import { Button, Card, Col, Row, message, Tabs } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import { useParams, useNavigate, useSearchParams } from "react-router-dom";
import type { ClusterDTO } from "@/api/dto";
import Loading from "@/components/Loading";
import ExpandNodeModal from "./ExpandNodeModal";
import NodeHealthCard from "./NodeHealthCard";
import NodeList from "./NodeList";
import NodeStatusPanel from "./NodeStatusPanel"; // ⬅️ 新增
import { useUser } from "@/store/user";
import { getCluster} from "@/api/modules";

const ClusterDetail: React.FC = () => {
  const { clusterId } = useParams();
  const id = Number(clusterId);
  const [sp, setSp] = useSearchParams();
  const navigate = useNavigate();

  const { userInfo } = useUser();
  const canEdit = ["admin", "ops"].includes(userInfo?.role || "");

  const [cluster, setCluster] = useState<ClusterDTO | null>(null);
  const [loading, setLoading] = useState(false);
  const [expandOpen, setExpandOpen] = useState(false);
  const [healthStats, setHealthStats] = useState({ healthy: 0, warning: 0, error: 0 });

  const activeTab = sp.get("tab") || "nodes"; // "nodes" | "status"

  const fetchDetail = async () => {
    setLoading(true);
    try {
      const res = await getCluster(id);
      setCluster(res?.data ?? res);
    } finally {
      setLoading(false);
    }
  };
  const fetchHealth = async () => {
    const res = await getNodeHealth(id);
    const data = res?.data ?? res ?? { healthy: 0, warning: 0, error: 0 };
    setHealthStats(data);
  };

  useEffect(() => {
    if (!id) return;
    fetchDetail();
    fetchHealth();
  }, [id]);

  const handleExpandSuccess = (result?: { success: number; fail: number; failList?: any[] }) => {
    setExpandOpen(false);
    fetchHealth();
    if (!result) return;
    let tip = `扩容成功${result.success}台`;
    if (result.fail > 0) tip += `，失败${result.fail}台`;
    message.success(tip);
    if (result.failList?.length) {
      message.warning(`失败详情：${result.failList.map((f) => `${f.name}(${f.reason})`).join(", ")}`);
    }
  };

  if (!id || loading || !cluster) return <Loading />;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-4">
        <button className="text-blue-600" onClick={() => navigate("/cluster/list")}>
          ← 返回列表
        </button>
        <Button type="primary" icon={<PlusOutlined />} onClick={() => setExpandOpen(true)} disabled={!canEdit}>
          节点扩容
        </Button>
      </div>

      <h3 className="font-bold text-xl mb-4">{cluster.name}</h3>

      <Row gutter={16}>
        <Col span={8}>
          <Card>
            <b>基础信息</b>
            <div className="mb-2"><b>类型：</b>{cluster.type}</div>
            <div className="mb-2"><b>地址：</b>{cluster.endpoint}</div>
            <div className="mb-2"><b>版本：</b>{cluster.version}</div>
            <div className="mb-2"><b>标签：</b>{cluster.tags}</div>
            <div className="mb-2"><b>备注：</b>{cluster.remark}</div>
            <div className="mb-2"><b>健康状态：</b>{cluster.status === 1 ? <span className="text-green-600">在线</span> : <span className="text-gray-400">下线</span>}</div>
          </Card>
          <Card className="mt-4">
            <NodeHealthCard stats={healthStats} />
          </Card>
        </Col>

        <Col span={16}>
          <Card>
            <Tabs
              activeKey={activeTab}
              onChange={(k) => {
                const next = new URLSearchParams(sp);
                next.set("tab", k);
                setSp(next, { replace: true });
              }}
              items={[
                {
                  key: "nodes",
                  label: "节点明细",
                  children: <NodeList clusterId={id} />,
                },
                {
                  key: "status",
                  label: "节点状态",
                  children: <NodeStatusPanel clusterId={id} />,
                },
              ]}
            />
          </Card>
        </Col>
      </Row>

      {expandOpen && (
        <ExpandNodeModal
          open={expandOpen}
          onOk={handleExpandSuccess}
          onClose={() => setExpandOpen(false)}
          clusterId={id}
        />
      )}
    </div>
  );
};

export default ClusterDetail;
