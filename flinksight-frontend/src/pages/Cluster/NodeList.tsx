/**
 * @file 节点列表（组件版）
 * @desc 传入 clusterId，自行拉取该集群的节点；包含明细弹窗与批量操作
 */
import React, { useEffect, useState } from "react";
import { Button, Input, message, Modal, Space, Table, Tag, Tooltip } from "antd";
import type { NodeDTO } from "@/api/dto";
import NodeDetailModal from "./NodeDetailModal";
import { useUser } from "@/store/user";
// import { listNodes, deleteNode } from "@/api/modules";                      // [KEEP]
import { getNodes, deleteNode } from "@/api/modules";                          // [FIX]

const { Search } = Input;

interface Props {
  clusterId: number;
}

const NodeList: React.FC<Props> = ({ clusterId }) => {
  const { userInfo } = useUser();
  const canEdit = ["admin", "ops"].includes(userInfo?.role || "");

  const [list, setList] = useState<NodeDTO[]>([]);
  const [page, setPage] = useState(1);   // 前端 1-based
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<{ keyword?: string }>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [detailId, setDetailId] = useState<number | null>(null);

  const fetch = async () => {
    if (!clusterId || !Number.isFinite(clusterId)) return;
    setLoading(true);
    try {
      // ✅ OpenAPI：对象入参 + query
      const q: any = { page, size };
      if (query.keyword) q.keyword = query.keyword;

      let res = await getNodes({ clusterId }, q);                 // /api/cluster/nodes/:clusterId
      let payload = (res as any)?.data?.data ?? (res as any)?.data ?? res;
      let rows =
        (Array.isArray(payload?.data)    && payload.data) ||
        (Array.isArray(payload?.records) && payload.records) ||
        (Array.isArray(payload?.list)    && payload.list) ||
        (Array.isArray(payload) ? payload : []);
      let totalNum =
        payload?.total ?? payload?.totalElements ?? payload?.totalCount ?? (Array.isArray(rows) ? rows.length : 0);

      // 若为空，尝试 0-based（少数后端）
      if ((!rows || rows.length === 0) && Number(totalNum) === 0) {
        const q0: any = { page: Math.max(0, page - 1), size };
        if (query.keyword) q0.keyword = query.keyword;
        res = await getNodes({ clusterId }, q0);
        payload = (res as any)?.data?.data ?? (res as any)?.data ?? res;
        rows =
          (Array.isArray(payload?.data)    && payload.data) ||
          (Array.isArray(payload?.records) && payload.records) ||
          (Array.isArray(payload?.list)    && payload.list) ||
          (Array.isArray(payload) ? payload : []);
        totalNum =
          payload?.total ?? payload?.totalElements ?? payload?.totalCount ?? (Array.isArray(rows) ? rows.length : 0);
      }

      setList(Array.isArray(rows) ? (rows as NodeDTO[]) : []);
      setTotal(Number(totalNum) || 0);
    } catch (e: any) {
      console.error("[NodeList] fetch error:", e);
      message.error(e?.message || "加载失败");
      setList([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetch(); }, [clusterId, page, size, query.keyword]);

  const handleSearch = (val: string) => {
    setQuery({ keyword: val?.trim() || undefined });
    setPage(1);
  };

  const openDetail = (id: number) => setDetailId(id);

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
          {/* 批量操作逻辑保留 // [KEEP] */}
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
          onChange: (p, s) => { setPage(p); setSize(s || size); },
        }}
        columns={[
          {
            title: "节点名",
            dataIndex: "name",
            render: (v: string, n: any) => (
              <Tooltip title="点击查看明细">
                <span className="text-blue-600 cursor-pointer" onClick={() => openDetail(n.id)}>{v}</span>
              </Tooltip>
            ),
          },
          { title: "IP", dataIndex: "ip" },
          { title: "角色", dataIndex: "type" }, // [KEEP] 你的表是 type(worker/nm)
          {
            title: "健康",
            dataIndex: "health",
            render: (v: string) => {
              const hv = String(v || "").toLowerCase();
              return (
                <Tag color={hv === "healthy" ? "green" : hv === "warning" ? "orange" : "red"}>
                  {hv === "healthy" ? "健康" : hv === "warning" ? "预警" : "异常"}
                </Tag>
              );
            },
          },
          {
            title: "状态",
            dataIndex: "status",
            render: (v: number | boolean) => {
              const on = typeof v === "boolean" ? v : Number(v) === 1;
              return on ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag>;
            },
          },
          {
            title: "操作",
            render: (_: any, n: any) => (
              <Space>
                <Button type="link" size="small" onClick={() => openDetail(n.id)}>明细</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(n.id)} disabled={!canEdit}>删除</Button>
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
