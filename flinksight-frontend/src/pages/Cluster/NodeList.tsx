/**
 * @file 节点列表（组件版）
 * @desc 传入 clusterId，自行拉取该集群的节点；包含明细弹窗与批量操作
 */
import React, { useEffect, useState } from "react";
import { Button, Input, message, Modal, Space, Table, Tag, Tooltip } from "antd";
import type { NodeDTO } from "@/api/dto";
import NodeDetailModal from "./NodeDetailModal";
import { useUser } from "@/store/user";
// ⛳ 改：统一使用 getNodes（与 NodeStatusPanel 一致）
import { getNodes, deleteNode } from "@/api/modules";

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

  function unpack(resp: any) {
    const payload = resp?.data?.data ?? resp?.data ?? resp;
    const rows =
      (Array.isArray(payload?.data)    && payload.data) ||
      (Array.isArray(payload?.records) && payload.records) ||
      (Array.isArray(payload?.list)    && payload.list) ||
      (Array.isArray(payload) ? payload : []);
    const totalNum =
      payload?.total ?? payload?.totalElements ?? payload?.totalCount ?? (Array.isArray(rows) ? rows.length : 0);
    return { rows: Array.isArray(rows) ? rows : [], total: Number(totalNum) || 0 };
  }

  const fetch = async () => {
    if (!clusterId) return;
    setLoading(true);
    try {
      // ⛳ 改：先尝试 1-based；若返回空且 total=0，再回退 0-based
      const q: any = { page, size };
      if (query.keyword) q.keyword = query.keyword;

      let res = await getNodes({ clusterId }, q);
      let { rows, total } = unpack(res);

      if (!rows.length && total === 0) {
        const q0: any = { page: Math.max(0, page - 1), size };
        if (query.keyword) q0.keyword = query.keyword;
        res = await getNodes({ clusterId }, q0);
        const r2 = unpack(res);
        rows = r2.rows; total = r2.total;
      }

      setList(rows as any);
      setTotal(total);
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

  // 只认 health 字段；没有就显示 -
  const renderHealth = (v: unknown) => {
    const s = String(v ?? "").toUpperCase();
    if (!s) return <Tag>-</Tag>;
    if (s === "HEALTHY")  return <Tag color="green">健康</Tag>;
    if (s === "WARNING")  return <Tag color="orange">预警</Tag>;
    return <Tag color="red">异常</Tag>; // 其它一律按“异常”
  };

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
          {/* 批量启停入口占位 */}
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
          // 你的表是 type(worker/nm)，之前写的是 role；保持你当前字段
          { title: "角色", dataIndex: "type" },
          // ⛳ 改：严格只按 health 字段展示
          { title: "健康", dataIndex: "health", render: renderHealth },
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
