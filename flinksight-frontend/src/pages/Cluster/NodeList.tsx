/**
 * @file 节点列表（组件版）
 * @desc 传入 clusterId，自行拉取该集群的节点；包含明细弹窗与批量操作
 */
import React, { useEffect, useState } from "react";
import { Button, Input, message, Modal, Space, Table, Tag, Tooltip } from "antd";
import type { NodeDTO } from "@/api/dto";
import NodeDetailModal from "./NodeDetailModal";
import { useUser } from "@/store/user";
import {getNodes,listNodes,deleteNode } from "@/api/modules";

const { Search } = Input;

interface Props {
  clusterId: number;
}

const NodeList: React.FC<Props> = ({ clusterId }) => {
  const { userInfo } = useUser();
  const canEdit = ["admin", "ops"].includes(userInfo?.role || "");

  const [list, setList] = useState<NodeDTO[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<{ keyword?: string }>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [detailId, setDetailId] = useState<number | null>(null);

  const fetch = async () => {
    if (!clusterId) return;
    setLoading(true);
    try {
      // 两种接口选一种：A) getNodesByCluster(id, { page, size, keyword })；B) getAllNodes({ clusterId, ... })
      const res = await listNodes(clusterId, { page, size, keyword: query.keyword })
      const data = res?.data;
      setList(data?.records || data?.list || []);
      setTotal(data?.total || data?.count || 0);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, [clusterId, page, size, query]);

  const handleSearch = (val: string) => {
    setQuery({ keyword: val });
    setPage(1);
  };

  const openDetail = (id: number) => setDetailId(id);

  async function handleBatchEnable(enable: boolean) {
    await getNodes(selectedRowKeys, enable);
    message.success(enable ? "已启用" : "已禁用");
    setSelectedRowKeys([]);
    fetch();
  }

  async function handleDelete(id: number) {
    Modal.confirm({
      title: "确认删除该节点？",
      onOk: async () => {
        await deleteNode(id);
        message.success("已删除");
        fetch();
      },
    });
  }

  return (
    <div>
      <div className="flex justify-between mb-4">
        <Search
          placeholder="节点名/IP/关键字"
          allowClear
          enterButton
          onSearch={handleSearch}
          style={{ width: 320 }}
        />
        <Space>
          <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length || !canEdit}>
            批量启用
          </Button>
          <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length || !canEdit}>
            批量禁用
          </Button>
        </Space>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        dataSource={list}
        rowSelection={{
          selectedRowKeys,
          onChange: (keys) => setSelectedRowKeys(keys as number[]),
        }}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          onChange: (p, s) => { setPage(p); setSize(s); },
        }}
        columns={[
          {
            title: "节点名",
            dataIndex: "name",
            render: (v: string, n: any) => (
              <Tooltip title="点击查看明细">
                <span className="text-blue-600 cursor-pointer" onClick={() => openDetail(n.id)}>
                  {v}
                </span>
              </Tooltip>
            ),
          },
          { title: "IP", dataIndex: "ip" },
          { title: "角色", dataIndex: "role" },
          { title: "CPU", dataIndex: "cpuUsage", render: (v: number) => `${v}%` },
          { title: "内存", dataIndex: "memUsage", render: (v: number) => `${v}%` },
          {
            title: "健康",
            dataIndex: "health",
            render: (v: string) => (
              <Tag color={v === "healthy" ? "green" : v === "warning" ? "orange" : "red"}>
                {v === "healthy" ? "健康" : v === "warning" ? "预警" : "异常"}
              </Tag>
            ),
          },
          { title: "状态", dataIndex: "enabled", render: (v: boolean) => (v ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag>) },
          {
            title: "操作",
            render: (_: any, n: any) => (
              <Space>
                <Button type="link" size="small" onClick={() => openDetail(n.id)}>
                  明细
                </Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(n.id)} disabled={!canEdit}>
                  删除
                </Button>
              </Space>
            ),
          },
        ]}
      />

      {detailId && (
        <NodeDetailModal id={detailId} open={!!detailId} onClose={() => setDetailId(null)} />
      )}
    </div>
  );
};

export default NodeList;
