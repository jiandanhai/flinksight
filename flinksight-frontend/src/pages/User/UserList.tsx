/**
 * @file 用户管理列表
 * @desc 支持分页、搜索、批量启用禁用、角色分配、用户详情/编辑，API/types全联动
 */
import React, { useEffect, useState } from 'react';
import { Table, Button, Input, Space, Modal, Tag, Select, message } from 'antd';
import { getUsers, updateUser, deleteUser, batchUpdateUserRole, batchEnableUsers } from '../../api/user';
import type { User, UserQuery, UserRole } from '../../types/user';
import EditUserModal from './EditUserModal';
import UserDetail from './UserDetail';
import { useUser } from '../../store/user';

const { Search } = Input;
const { Option } = Select;

const ROLE_LABELS: Record<UserRole, string> = {
  admin: '管理员',
  ops: '运维',
  user: '普通用户'
};

const UserList: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(20);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState<UserQuery>({});
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);
  const [modalVisible, setModalVisible] = useState(false);
  const [editId, setEditId] = useState<number | null>(null);
  const [detailId, setDetailId] = useState<number | null>(null);
  const { role } = useUser();

  const canEdit = role === 'admin';

  // 拉取用户列表
  const fetchUsers = async () => {
    setLoading(true);
    try {
      const res = await getUsers({ ...query, page, size });
      setUsers(res.data?.records || []);
      setTotal(res.data?.total || 0);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetchUsers(); }, [query, page, size]);

  // 搜索
  const handleSearch = (val: string) => {
    setQuery({ ...query, keyword: val });
    setPage(1);
  };

  // 新建/编辑
  function openModal(id?: number) {
    setEditId(id || null);
    setModalVisible(true);
  }

  // 详情页
  if (detailId) {
    return <UserDetail id={detailId} onBack={() => setDetailId(null)} />;
  }

  // 删除
  async function handleDelete(id: number) {
    Modal.confirm({
      title: '确认删除该用户？',
      onOk: async () => {
        await deleteUser(id);
        message.success('已删除');
        fetchUsers();
      }
    });
  }

  // 批量启用/禁用
  async function handleBatchEnable(enable: boolean) {
    await batchEnableUsers(selectedRowKeys, enable);
    message.success(enable ? '已启用' : '已禁用');
    setSelectedRowKeys([]);
    fetchUsers();
  }

  // 批量分配角色
  async function handleBatchRole(newRole: UserRole) {
    await batchUpdateUserRole(selectedRowKeys, newRole);
    message.success('角色分配完成');
    setSelectedRowKeys([]);
    fetchUsers();
  }

  return (
    <div className="p-6 bg-white rounded-xl shadow">
      <div className="flex justify-between mb-4">
        <Search placeholder="用户名/昵称/邮箱" allowClear enterButton onSearch={handleSearch} style={{ width: 320 }} />
        <Space>
          <Button type="primary" onClick={() => openModal()} disabled={!canEdit}>新建用户</Button>
          <Button onClick={() => handleBatchEnable(true)} disabled={!selectedRowKeys.length || !canEdit}>批量启用</Button>
          <Button danger onClick={() => handleBatchEnable(false)} disabled={!selectedRowKeys.length || !canEdit}>批量禁用</Button>
          <Select
            placeholder="批量分配角色"
            style={{ width: 120 }}
            onChange={role => handleBatchRole(role as UserRole)}
            disabled={!selectedRowKeys.length || !canEdit}
            allowClear
          >
            <Option value="admin">管理员</Option>
            <Option value="ops">运维</Option>
            <Option value="user">普通用户</Option>
          </Select>
        </Space>
      </div>
      <Table
        rowKey="id"
        dataSource={users}
        loading={loading}
        rowSelection={{
          selectedRowKeys,
          onChange: (keys) => setSelectedRowKeys(keys as number[])
        }}
        pagination={{
          current: page,
          pageSize: size,
          total,
          showSizeChanger: true,
          onChange: (p, s) => { setPage(p); setSize(s); }
        }}
        columns={[
          {
            title: '用户名',
            dataIndex: 'username',
            render: (_: any, u: User) =>
              <span className="text-blue-600 cursor-pointer" onClick={() => setDetailId(u.id)}>{u.username}</span>
          },
          { title: '昵称', dataIndex: 'nickname' },
          { title: '邮箱', dataIndex: 'email' },
          { title: '角色', dataIndex: 'role', render: (r: UserRole) => <Tag>{ROLE_LABELS[r]}</Tag> },
          { title: '状态', dataIndex: 'enabled', render: (v: boolean) => v ? <Tag color="green">启用</Tag> : <Tag color="red">禁用</Tag> },
          { title: '创建时间', dataIndex: 'createTime', render: (v: string) => new Date(v).toLocaleString() },
          {
            title: '操作',
            render: (_: any, u: User) => (
              <Space>
                <Button type="link" size="small" onClick={() => openModal(u.id)} disabled={!canEdit}>编辑</Button>
                <Button type="link" size="small" danger onClick={() => handleDelete(u.id)} disabled={!canEdit}>删除</Button>
              </Space>
            )
          }
        ]}
      />
      {modalVisible && (
        <EditUserModal
          id={editId}
          open={modalVisible}
          onOk={() => { setModalVisible(false); fetchUsers(); }}
          onClose={() => setModalVisible(false)}
        />
      )}
    </div>
  );
};
export default UserList;
