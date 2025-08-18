/**
 * @file 集群节点状态页面
 * @desc 展示各Flink/Spark集群的节点实时状态
 */
import React, {useEffect, useState} from "react";
import {Card, Table, Tag} from "antd";
import api from '@/api/api-compat';

const NodeStatus: React.FC = () => {
  const [data, setData] = useState<any[]>([]);
  useEffect(() => {
    api.getNodesByCluster(clusterId).then(res => setData(res.data));
  }, []);

  return (
    <Card title="集群节点状态">
      <Table
        rowKey="nodeId"
        dataSource={data}
        columns={[
          { title: "节点ID", dataIndex: "nodeId" },
          { title: "主机名", dataIndex: "host" },
          { title: "角色", dataIndex: "role", render: (v: string) => <Tag>{v}</Tag> },
          { title: "状态", dataIndex: "status", render: (v: string) =>
            v === "ONLINE"
              ? <Tag color="green">在线</Tag>
              : <Tag color="red">离线</Tag>
          },
          { title: "CPU", dataIndex: "cpu", render: (v: number) => <span>{v}%</span> },
          { title: "内存", dataIndex: "memory", render: (v: number) => <span>{v}MB</span> },
          { title: "最近心跳", dataIndex: "lastHeartbeat" }
        ]}
      />
    </Card>
  );
};
export default NodeStatus;
