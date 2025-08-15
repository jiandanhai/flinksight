/**
 * @file 用户新建/编辑弹窗
 * @desc 支持新增、编辑、重置密码，表单校验，API/types自动对接
 */
import React, {useEffect} from 'react';
import {Form, Input, message, Modal, Select, Switch} from 'antd';
import  api  from 'src/api/gen/client';

import type {UserDTO} from '../../api/gen/data-contracts.ts';

const { Option } = Select;

interface Props {
  id?: number | null;
  open: boolean;
  onOk: () => void;
  onClose: () => void;
}

const EditUserModal: React.FC<Props> = ({ id, open, onOk, onClose }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (id) {
      api.getUser(id).then(res => form.setFieldsValue(res.data));
    } else {
      form.resetFields();
    }
  }, [id, form]);

  // 提交
  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (id) {
      await api.updateUser(id, values as UserDTO);
      message.success('编辑成功');
    } else {
      await api.createUser(values as UserDTO);
      message.success('新建成功');
    }
    onOk();
    onClose();
  };

  // 重置密码（仅编辑时可见）
  const handleResetPwd = async () => {
    if (!id) return;
    await api.resetUserPassword(id);
    message.success('已重置密码（新密码请通过通知渠道获取）');
  };

  return (
    <Modal
      title={id ? '编辑用户' : '新建用户'}
      open={open}
      onOk={handleSubmit}
      onCancel={onClose}
      destroyOnClose
      maskClosable={false}
      footer={[
        id && <Button key="resetPwd" danger onClick={handleResetPwd}>重置密码</Button>,
        <Button key="cancel" onClick={onClose}>取消</Button>,
        <Button key="ok" type="primary" onClick={handleSubmit}>保存</Button>
      ]}
    >
      <Form form={form} layout="vertical">
        <Form.Item name="username" label="用户名" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={32} placeholder="用户名" disabled={!!id} />
        </Form.Item>
        <Form.Item name="nickname" label="昵称" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={32} placeholder="昵称" />
        </Form.Item>
        <Form.Item name="email" label="邮箱" rules={[{ type: 'email', required: true, message: '邮箱格式不正确' }]}>
          <Input placeholder="邮箱" />
        </Form.Item>
        <Form.Item name="role" label="角色" rules={[{ required: true, message: '必选' }]}>
          <Select>
            <Option value="admin">管理员</Option>
            <Option value="ops">运维</Option>
            <Option value="user">普通用户</Option>
          </Select>
        </Form.Item>
        <Form.Item name="enabled" label="启用状态" valuePropName="checked" initialValue={true}>
          <Switch checkedChildren="启用" unCheckedChildren="禁用" />
        </Form.Item>
        {!id && (
          <Form.Item name="password" label="初始密码" rules={[{ required: true, min: 6, message: '最少6位' }]}>
            <Input.Password placeholder="初始密码" />
          </Form.Item>
        )}
      </Form>
    </Modal>
  );
};
export default EditUserModal;
