/**
 * @file 权限与菜单管理
 * @desc 配置菜单/按钮级权限，支持租户、角色切换，批量保存
 */
import React, { useEffect, useState } from 'react';
import { Button, message, Table, Tree, Spin } from 'antd';
// 推荐用openapi自动生成api
import { api } from 'src/api/gen/client';

const PermissionManager: React.FC = () => {
  const [permissions, setPermissions] = useState<any[]>([]);
  const [selectedKeys, setSelectedKeys] = useState<React.Key[]>([]);
  const [roles, setRoles] = useState<any[]>([]);
  const [activeRole, setActiveRole] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchRolesAndPermissions();
  }, []);

  // 拉取角色列表和权限树
  const fetchRolesAndPermissions = async () => {
    setLoading(true);
    const [permRes, roleRes] = await Promise.all([
      api.permissionControllerTree(),
      api.roleControllerList()
    ]);
    setPermissions(permRes.data || []);
    setRoles(roleRes.data || []);
    setLoading(false);
  };

  // 选择角色后加载权限
  const handleRoleSelect = async (roleId: number) => {
    setActiveRole(roleId);
    setLoading(true);
    const res = await api.roleControllerGetPermissions({ id: roleId });
    setSelectedKeys(res.data?.map((item: any) => item.id) || []);
    setLoading(false);
  };

  // 保存角色权限
  const handleSave = async () => {
    if (activeRole) {
      await api.roleControllerSetPermissions({ id: activeRole, body: { permissionIds: selectedKeys } });
      message.success('权限配置已更新');
    }
  };

  return (
    <Spin spinning={loading}>
      <div style={{ display: 'flex' }}>
        {/* 左侧角色表 */}
        <Table
          dataSource={roles}
          rowKey="id"
          columns={[{ title: '角色名', dataIndex: 'name' }]}
          pagination={false}
          onRow={record => ({
            onClick: () => handleRoleSelect(record.id),
            style: { cursor: 'pointer', background: record.id === activeRole ? '#e6f7ff' : undefined }
          })}
          style={{ width: 220, marginRight: 32 }}
        />
        {/* 右侧权限树 */}
        <div style={{ flex: 1 }}>
          <h4>菜单与按钮权限分配</h4>
          <Tree
            checkable
            treeData={permissions}
            checkedKeys={selectedKeys}
            onCheck={checked => setSelectedKeys(checked as React.Key[])}
            fieldNames={{ title: 'name', key: 'id', children: 'children' }}
            defaultExpandAll
          />
          <Button style={{ marginTop: 16 }} type="primary" onClick={handleSave}>保存</Button>
        </div>
      </div>
    </Spin>
  );
};

export default PermissionManager;
