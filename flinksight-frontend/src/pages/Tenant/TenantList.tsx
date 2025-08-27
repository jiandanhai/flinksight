/**
 * @file 租户管理
 * @desc 新增、编辑、启/停用、删除、分页、搜索（兼容多种分页字段）
 */
import React, { useEffect, useMemo, useState } from 'react';
import { Button, Input, message, Modal, Space, Table, Tag } from 'antd';
import { listTenants, updateTenant, deleteTenant } from '@/api/modules';
import type { TenantDTO } from '@/api/dto';
import EditTenantModal from './EditTenantModal';
import { useUser } from '../../store/user';

type TenantQuery = { keyword?: string; enabled?: boolean };

// 统一解包：兼容 openapi 客户端的 { data: { data: ... } } / { data: ... } / 直接返回
const pickPayload = (r: any) => (r?.data?.data ?? r?.data ?? r);

// 统一解析分页：兼容 records/items/content/list/data/rows + total/totalCount/totalElements
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
  return { rows: rows as TenantDTO[], total: Number(total) || 0 };
};

const TenantList: React.FC = () => {
  const [list, setList] = useState<TenantDTO[]>([]);
  const [page, setPage] = useState(1);  // Antd 1-based
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);

  const [query, setQuery] = useState<TenantQuery>({});
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);

  const { role } = useUser();
  const canEdit = role === 'admin';

  const fetch = async () => {
    setLoading(true);
    try {
      const res = await listTenants({ page: page - 1, size, keyword: query.keyword });
      const { rows, total } = parsePage(pickPayload(res));
      setList(rows);
      setTotal(total);
    } catch (e: any) {
      message.error(e?.message || '加载租户失败');
      setList([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  const queryKey = useMemo(() => JSON.stringify(query), [query]);
  useEffect(() => { fetch(); /* eslint-disable-next-line */ }, [page, size, queryKey]);

  // 搜索
  const handleSearch = (val: string) => {
    setQuery({ ...query, keyword: val?.trim() || undefined });
    setPage(1);
  };

  // 启/停用
  const handleEnable = (t: TenantDTO) => {
    if (!t.id) return;
    Modal.confirm({
      title: `确认${t.enabled ? '禁用' : '启用'}该租户？`,
      onOk: async () => {
        try {
          await updateTenant(t.id!, { enabled: !t.enabled });
          message.success(t.enabled ? '已禁用' : '已启用');
          fetch();
        } catch (e: any) {
          message.error(e?.message || '操作失败');
        }
      },
    });
  };

  // 删除
  const handleDelete = (id: number) => {
    Modal.confirm({
      title: '确认删除该租户？',
      onOk: async () => {
        try {
          await deleteTenant(id);
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
      <div className="flex justify-between mb-4 gap-2">
        <Input.Search
          allowClear
          placeholder="按租户名/邮箱搜索"
          onSearch={handleSearch}
          style={{ maxWidth: 320 }}
        />
        <Button
          type="primary"
          onClick={() => { setEditId(null); setModalVisible(true); }}
          disabled={!canEdit}
        >
          新增租户
        </Button>
      </div>

      <Table<TenantDTO>
        rowKey={(r) => (r as any).id ?? (r as any).tenantId}  // 兼容不同主键名
        dataSource={list}
        loading={loading}
        columns={[
          { title: '租户名', dataIndex: 'name' },
          { title: '管理员', dataIndex: 'admin' },
          { title: '邮箱', dataIndex: 'email' },
          {
            title: '状态',
            dataIndex: 'enabled',
            render: (v: boolean) => (v ? <Tag color="green">启用</Tag> : <Tag>禁用</Tag>),
          },
          {
            title: '操作',
            width: 220,
            render: (_: any, t: TenantDTO) => (
              <Space>
                <Button
                  type="link"
                  size="small"
                  onClick={() => { setEditId(t.id!); setModalVisible(true); }}
                  disabled={!canEdit}
                >
                  编辑
                </Button>
                <Button
                  type="link"
                  size="small"
                  onClick={() => handleEnable(t)}
                  disabled={!canEdit}
                >
                  {t.enabled ? '禁用' : '启用'}
                </Button>
                <Button
                  type="link"
                  size="small"
                  danger
                  onClick={() => handleDelete(t.id!)}
                  disabled={!canEdit}
                >
                  删除
                </Button>
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
        <EditTenantModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetch(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};

export default TenantList;
