/**
 * @file 节点列表（组件版）
 * @desc 传入 clusterId，自行拉取该集群的节点；包含明细弹窗与批量操作
 */
import React, { useEffect, useRef, useState } from "react";
import { Button, Input, message, Modal, Space, Table, Tag, Tooltip } from "antd";
import type { NodeDTO } from "@/api/dto";
import NodeDetailModal from "./NodeDetailModal";
import { useUser } from "@/store/user";
import { getNodes, deleteNode } from "@/api/modules";

const { Search } = Input;

interface Props {
  clusterId: number;
}

const ui2apiPage = (uiPage: number) => Math.max(0, Number(uiPage) - 1);

function unpackPageLike(resp: any) {
  const root = resp?.data ?? resp;
  const outer = root?.data !== undefined ? root.data : root;
  const keys = ['data','records','list','rows','items','content','result'];

  if (Array.isArray(outer)) return { rows: outer, total: outer.length };

  for (const k of keys) {
    const v = outer?.[k];
    if (Array.isArray(v)) {
      const total = Number(outer?.total ?? outer?.totalElements ?? outer?.totalCount ?? outer?.count ?? v.length) || v.length;
      return { rows: v, total };
    }
  }

  const d = outer?.data;
  if (d && typeof d === 'object') {
    for (const k of keys) {
      const v = d?.[k];
      if (Array.isArray(v)) {
        const total = Number(d?.total ?? d?.totalElements ?? d?.totalCount ?? d?.count ?? v.length) || v.length;
        return { rows: v, total };
      }
    }
  }

  if (outer && typeof outer === 'object') {
    for (const [, v] of Object.entries(outer)) {
      if (Array.isArray(v)) {
        const total = Number(outer?.total ?? outer?.totalElements ?? outer?.totalCount ?? outer?.count ?? (v as any[]).length) || (v as any[]).length;
        return { rows: v as any[], total };
      }
    }
  }
  return { rows: [] as any[], total: 0 };
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
  const [selectedRowKeys, setSelectedRowKeys] = useState<(number | string)[]>([]);
  const [detailId, setDetailId] = useState<number | string | null>(null);

  const seqRef = useRef(0);

  const fetch = async () => {
    if (!clusterId) return;
    setLoading(true);
    const seq = ++seqRef.current;
    try {
      const q: any = { page: ui2apiPage(page), size };
      if (query.keyword) q.keyword = query.keyword;

      const res = await getNodes({ clusterId }, q);
      const { rows, total } = unpackPageLike(res);

      if (seq !== seqRef.current) return;
      setList(rows as any);
      setTotal(total);
    } catch (e: any) {
      if (seq !== seqRef.current) return;
      console.error("[NodeList] fetch error:", e);
      message.error(e?.message || "加载失败");
      setList([]);
      setTotal(0);
    } finally {
      if (seq === seqRef.current) setLoading(false);
    }
  };

  useEffect(() => { fetch(); }, [clusterId, page, size, query.keyword]);

  const handleSearch = (val: string) => {
    setQuery({ keyword: val?.trim() || undefined });
    setPage(1);
  };

  const openDetail = (id: number | string) => setDetailId(id);

  async function handleDelete(id: number | string) {
    Modal.confirm({
      title: "确认删除该节点？",
      onOk: async () => {
        await deleteNode(id as any);
        message.success("已删除");
        fetch();
      },
    });
  }

  const renderHealth = (v: unknown) => {
    const s = String(v ?? "").toUpperCase();
    if (!s) return <Tag>-</Tag>;
    if (s === "HEALTHY")  return <Tag color="green">健康</Tag>;
    if (s === "WARNING")  return <Tag color="orange">预警</Tag>;
    return <Tag color="red">异常</Tag>;
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
        <Space>{/* 保留占位 */}</Space>
      </div>

      <Table
        rowKey="id"
        loading={loading}
        dataSource={list}
        rowSelection={{
          selectedRowKeys,
          onChange: (keys) => setSelectedRowKeys(keys as (number | string)[]),
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
          { title: "角色", dataIndex: "type" },
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
        <NodeDetailModal id={detailId as any} open={!!detailId} onClose={() => setDetailId(null)} />
      )}
    </div>
  );
};

export default NodeList;
