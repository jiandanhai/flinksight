/**
 * @file 角色管理（统一为与租户/用户相同的 UI 风格）
 * @desc 搜索 / 分页 / 新建 / 编辑 / 删除
 */
import React, { useEffect, useMemo, useState } from 'react';
import { Button, Input, message, Modal, Space, Table } from 'antd';
import { listRoles, deleteRole } from '@/api/modules';
import type { RoleDTO } from '@/api/dto';
import EditRoleModal from './EditRoleModal';

type RoleQuery = { keyword?: string };

// —— 兼容 openapi 客户端的多种响应包装
const pickPayload = (r: any) => (r?.data?.data ?? r?.data ?? r);
// —— 兼容多种分页字段命名
const parsePage = (payload: any) => {
  const rows =
    (Array.isArray(payload?.records) && payload.records) ||
    (Array.isArray(payload?.items) && payload.items) ||
    (Array.isArray(payload?.content) && payload.content) ||
    (Array.isArray(payload?.list) && payload.list) ||
    (Array.isArray(payload?.data) && payload.data) ||
    (Array.isArray(payload?.rows) && payload.rows) ||
    (Array.isArray(payload) ? payload : []) ||
    [];
  const total =
    payload?.total ??
    payload?.totalCount ??
    payload?.totalElements ??
    payload?.count ??
    rows.length;
  return { rows: rows as RoleDTO[], total: Number(total) || 0 };
};

const RoleList: React.FC = () => {
  const [list, setList] = useState<RoleDTO[]>([]);
  const [page, setPage] = useState(1);   // Antd 1-based
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  const [query, setQuery] = useState<RoleQuery>({});
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);

  const fetch = async () => {
    setLoading(true);
    try {
      const res = await listRoles({ page: page - 1, size, keyword: query.keyword });
      const { rows, total } = parsePage(pickPayload(res));
      setList(rows);
      setTotal(total);
    } catch (e: any) {
      message.error(e?.message || '加载角色失败');
      setList([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  const qKey = useMemo(() => JSON.stringify(query), [query]);
  useEffect(() => { fetch(); /* eslint-disable-next-line */ }, [page, size, qKey]);

  const openModal = (id?: number) => { setEditId(id ?? null); setModalVisible(true); };

  const handleDelete = (r: RoleDTO) => {
    if (!r.id) return;
    Modal.confirm({
      title: `确认删除角色「${r.name ?? r.id}」？`,
      onOk: async () => {
        try {
          await deleteRole(r.id!);
          message.success('已删除');
          fetch();
        } catch (e: any) {
          message.error(e?.message || '删除失败');
        }
      },
    });
  };

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      {/* 顶部工具条：搜索 + 新建 */}
      <div className="flex justify-between mb-4 gap-2">
        <Input.Search
          allowClear
          placeholder="按角色名/标识搜索"
          onSearch={(val) => {
            setQuery({ keyword: val?.trim() || undefined });
            setPage(1);
          }}
          style={{ maxWidth: 320 }}
        />
        <Button type="primary" onClick={() => openModal()}>新建角色</Button>
      </div>

      <Table<RoleDTO>
        rowKey="id"
        dataSource={list}
        loading={loading}
        columns={[
          {
            title: '角色名',
            dataIndex: 'name',
            render: (v: any) => v ?? '-',
          },
          { title: '标识', dataIndex: 'code', render: (v: any) => v ?? '-' },
          {
            title: '描述',
            dataIndex: 'desc',
            render: (_: any, r) => (r as any).desc ?? (r as any).description ?? '-',
          },
          {
            title: '操作',
            width: 200,
            render: (_: any, r) => (
              <Space>
                <Button type="link" size="small" onClick={() => openModal(r.id!)}>编辑</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(r)}>删除</Button>
              </Space>
            ),
          },
        ]}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          showTotal: (t) => `共 ${t} 条`,
          onChange: (p, s) => { setPage(p); setSize(s || size); },
        }}
      />

      {modalVisible && (
        <EditRoleModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};

export default RoleList;
