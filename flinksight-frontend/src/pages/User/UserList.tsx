/**
 * @file 用户管理（最小修改：不再依赖 tenantId 才发请求；兼容分页字段）
 */
import React, { useEffect, useMemo, useState } from 'react';
import { Input, Table, Tag, Button, message } from 'antd';
import { listUsers } from '@/api/modules';
import type { UserDTO } from '@/api/dto';
import { useUser } from '../../store/user';
import { getTenantId } from "@/utils/tenant";

type UserQuery = {
  keyword?: string;
  enabled?: boolean;
};

const UserList: React.FC = () => {
  // —— 解析租户ID（只用你提供的方法）
  const tenantId = useMemo(() => {
    try {
      const v = getTenantId();
      const n = Number(v);
      return Number.isFinite(n) ? n : undefined;
    } catch {
      return undefined;
    }
  }, []);

  const [list, setList] = useState<UserDTO[]>([]);
  const [page, setPage] = useState(1); // antd 从 1 开始
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<UserQuery>({});

  const queryKey = useMemo(() => JSON.stringify(query), [query]);

  const fetchUsers = async () => {
    setLoading(true);
    try {
      // ✅ 这里不再因 tenantId 为空而 return；有就加，没有就不传
      const params: any = { page: page - 1, size, ...query };
      if (tenantId) params.tenantId = tenantId;

      // 便于排查你能看到确实在发请求
      console.log('[UserList] GET /api/user/list params =', params);

      const res: any = await listUsers(params);

      // ✅ 兼容不同分页字段：records/items/content/list/data + total/totalCount/totalElements
      const rows =
        res?.records ??
        res?.items ??
        res?.content ??
        res?.list ??
        res?.data ??
        [];
      const totalCount =
        res?.total ??
        res?.totalCount ??
        res?.totalElements ??
        (Array.isArray(rows) ? rows.length : 0);

      setList(Array.isArray(rows) ? rows : []);
      setTotal(Number(totalCount) || 0);
    } catch (e: any) {
      console.error('[UserList] getUsersByTenant error:', e);
      message.error(e?.message || '加载用户失败');
      setList([]);
      setTotal(0);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, size, queryKey]); // ❗ 不把 tenantId 放依赖里，避免切换为空时“打断”请求

  const onSearch = (val: string) => {
    setQuery({ ...query, keyword: val?.trim() || undefined });
    setPage(1);
  };

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4 gap-2">
        <Input.Search
          allowClear
          placeholder="按用户名/邮箱搜索"
          onSearch={onSearch}
          style={{ maxWidth: 320 }}
        />
        <Button onClick={fetchUsers}>刷新</Button>
      </div>

      <Table<UserDTO>
        rowKey={(r) => (r as any).id ?? (r as any).userId ?? (r as any).username}
        dataSource={list}
        loading={loading}
        columns={[
          { title: '用户名', dataIndex: 'username' },
          { title: '昵称', dataIndex: 'nickname' },
          { title: '邮箱', dataIndex: 'email' },
          {
            title: '角色',
            dataIndex: 'roles',
            render: (arr: any) =>
              Array.isArray(arr)
                ? (arr.map((x: any) => x?.name ?? x).filter(Boolean).join('、') || '-')
                : '-',
          },
          {
            title: '状态',
            dataIndex: 'enabled',
            render: (v: boolean) => (v ? <Tag color="green">启用</Tag> : <Tag>禁用</Tag>),
          },
        ]}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          showTotal: (t) => `共 ${t} 条`,
          onChange: (p, s) => {
            setPage(p);
            setSize(s || size);
          },
        }}
      />
    </div>
  );
};

export default UserList;
