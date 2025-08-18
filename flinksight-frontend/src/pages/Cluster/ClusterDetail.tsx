import React, {useEffect, useState} from 'react';
import {Button, Card, Col, message, Row} from 'antd';
import {PlusOutlined} from '@ant-design/icons';
import api from '@/api/api-compat';

import type {ClusterDTO, NodeDTO} from '@/api/dto';
import Loading from '../../components/Loading';
import ExpandNodeModal from './ExpandNodeModal';
import NodeHealthCard from './NodeHealthCard';
import NodeList from './NodeList';
import {useUser} from '../../store/user';

/**
 * 集群详情页
 * - 展示集群基础信息、健康分布、节点明细，支持节点扩容
 */
interface Props {
  id: number;
  onBack: () => void;
}

const ClusterDetail: React.FC<Props> = ({ id, onBack }) => {
  const [cluster, setCluster] = useState<ClusterDTO | null>(null);
  const [loading, setLoading] = useState(false);
  const [expandOpen, setExpandOpen] = useState(false);
  const [nodeList, setNodeList] = useState<NodeDTO[]>([]);
  const [healthStats, setHealthStats] = useState<{ healthy: number; warning: number; error: number }>({ healthy: 0, warning: 0, error: 0 });
  const { role } = useUser();
  const canEdit = role === 'admin' || role === 'ops';

  // 拉取集群信息
  const fetchDetail = () => {
    setLoading(true);
    api.getCluster(id)
      .then(data => setCluster(data))
      .finally(() => setLoading(false));
  };

  // 拉取节点列表和健康分布
  const fetchNodesAndHealth = () => {
    api.getNodesByCluster(id, { page: 1, size: 1000 }).then(res => setNodeList(res.data?.records || []));
    api.getNodeHealth(id).then(res => setHealthStats(res.data || { healthy: 0, warning: 0, error: 0 }));
  };

  // 初始化
  useEffect(() => {
    fetchDetail();
    fetchNodesAndHealth();
  }, [id]);

  // 扩容成功后自动刷新节点和健康
  const handleExpandSuccess = (result?: { success: number; fail: number; failList?: any[] }) => {
    setExpandOpen(false);
    fetchNodesAndHealth();
    if (!result) return;
    let tip = `扩容成功${result.success}台`;
    if (result.fail > 0) tip += `，失败${result.fail}台`;
    message.success(tip);
    if (result.failList?.length) {
      message.warning(`失败详情：${result.failList.map(f => `${f.name}(${f.reason})`).join(', ')}`);
    }
  };

  if (loading || !cluster) return <Loading />;

  return (
    <div className="p-8">
      {/* 节点扩容按钮 */}
      <div className="flex justify-end mb-4">
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setExpandOpen(true)}
          disabled={!canEdit}
        >
          节点扩容
        </Button>
      </div>
      {/* 返回列表 */}
      <button className="mb-6 text-blue-600" onClick={onBack}>← 返回列表</button>
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
            <NodeHealthCard clusterId={id} stats={healthStats} />
          </Card>
        </Col>
        <Col span={16}>
          <Card>
            <b>节点明细</b>
            <NodeList nodes={nodeList} clusterId={id} refresh={fetchNodesAndHealth} />
          </Card>
        </Col>
      </Row>
      {/* 节点扩容弹窗 */}
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
