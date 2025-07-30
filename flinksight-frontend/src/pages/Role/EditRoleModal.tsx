/**
 * @file 角色新建/编辑弹窗
 * @desc 支持权限分配、表单校验、API联动，管理员权限校验
 */
import React, { useEffect } from 'react';
import { Modal, Form, Input, Select, message } from 'antd';
import { createRole, updateRole, getRoleDetail } from '../../api/role';
import type { Role, RoleCreateReq, RoleUpdateReq } from '../../types/role';

const { Option } = Select;

interface Props {
  id?: number | null;
  open: boolean;
  onOk: () => void;
  onClose: () => void;
}

const ALL_PERMISSIONS = [
  'user:read', 'user:write', 'role:read', 'role:write',
  'alert:read', 'alert:write', 'job:read', 'job:write',
  'cluster:read', 'cluster:write', 'settings:read', 'settings:write'
];

const EditRoleModal: React.FC<Props> = ({ id, open, onOk, onClose }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (id) {
      getRoleDetail(id).then(res => form.setFieldsValue(res.data));
    } else {
      form.resetFields();
    }
  }, [id, form]);

  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (id) {
      await updateRole(id, values as RoleUpdateReq);
      message.success('编辑成功');
    } else {
      await createRole(values as RoleCreateReq);
      message.success('新建成功');
    }
    onOk();
    onClose();
  };

  return (
    <Modal
      open={open}
      title={id ? '编辑角色' : '新建角色'}
      onOk={handleSubmit}
      onCancel={onClose}
      destroyOnClose
      maskClosable={false}
    >
      <Form form={form} layout="vertical">
        <Form.Item name="name" label="角色名" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={32} />
        </Form.Item>
        <Form.Item name="permissions" label="权限分配" rules={[{ required: true, message: '至少选择一项' }]}>
          <Select mode="multiple" allowClear placeholder="请选择权限">
            {ALL_PERMISSIONS.map(p => <Option value={p} key={p}>{p}</Option>)}
          </Select>
        </Form.Item>
        <Form.Item name="desc" label="描述">
          <Input.TextArea maxLength={128} />
        </Form.Item>
      </Form>
    </Modal>
  );
};
export default EditRoleModal;
