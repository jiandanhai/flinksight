/**
 * @file 权限与菜单管理
 * @desc 配置菜单/按钮级权限，适配不同租户/角色
 */
import React, { useEffect, useState } from 'react';
import { Table, Tree, Button, Modal, message } from 'antd';
import http from '@/api/http';

interface Permission {
  id: number;
  name: string;
  code: string;
  children?: Permission[];
}

const PermissionManager: React.FC = () => {
  const [permissions, setPermissions] = useState<Permission[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<number[]>([]);
  const [roles, setRoles] = useState<{ id: number, name: string }[]>([]);
  const [activeRole, setActiveRole] = useState<number | null>(null);

  useEffect(() => {
    http.get('/permissions/tree').then(res => setPermissions(res.data || []));
    http.get('/roles').then(res => setRoles(res.data || []));
  }, []);

  const handleRoleSelect = async (roleId: number) => {
    setActiveRole(roleId);
    const res = await http.get(`/roles/${roleId}/permissions`);
    setSelectedKeys(res.data || []);
  };

  const handleSave = async () => {
    if (activeRole) {
      await http.post(`/roles/${activeRole}/permissions`, { permissionIds: selectedKeys });
      message.success('权限配置已更新');
    }
  };

  return (
    <div style={{ display: 'flex' }}>
      <Table
        dataSource={roles}
        rowKey="id"
        columns={[
          { title: '角色名', dataIndex: 'name' }
        ]}
        pagination={false}
        onRow={record => ({
          onClick: () => handleRoleSelect(record.id),
          style: { cursor: 'pointer', background: record.id === activeRole ? '#f0f0f0' : undefined }
        })}
        style={{ width: 200, marginRight: 32 }}
      />
      <div style={{ flex: 1 }}>
        <h4>菜单与按钮权限分配</h4>
        <Tree
          checkable
          treeData={permissions}
          checkedKeys={selectedKeys}
          onCheck={checked => setSelectedKeys(checked as number[])}
        />
        <Button style={{ marginTop: 16 }} type="primary" onClick={handleSave}>保存</Button>
      </div>
    </div>
  );
};

export default PermissionManager;
