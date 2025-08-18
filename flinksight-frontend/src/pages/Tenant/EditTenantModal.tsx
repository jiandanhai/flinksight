/**
 * @file 新建/编辑租户弹窗
 * @desc 支持租户基础信息、负责人、状态、表单校验
 */
import React, {useEffect} from 'react';
import {Form, Input, message, Modal, Switch} from 'antd';
import api from '@/api/api-compat';

import type {TenantDTO} from '@/api/dto';

interface Props {
  id?: number | null;
  open: boolean;
  onOk: () => void;
  onClose: () => void;
}

const EditTenantModal: React.FC<Props> = ({ id, open, onOk, onClose }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (id) {
      api.getTenant(id).then(res => form.setFieldsValue(res.data));
    } else {
      form.resetFields();
    }
  }, [id, form]);

  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (id) {
      await api.updateTenant(id, values as TenantDTO);
      message.success('编辑成功');
    } else {
      await api.createTenant(values as TenantDTO);
      message.success('新建成功');
    }
    onOk();
    onClose();
  };

  return (
    <Modal
      open={open}
      title={id ? '编辑租户' : '新建租户'}
      onOk={handleSubmit}
      onCancel={onClose}
      destroyOnClose
      maskClosable={false}
    >
      <Form form={form} layout="vertical">
        <Form.Item name="name" label="租户名" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={32} />
        </Form.Item>
        <Form.Item name="code" label="租户编码" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={32} />
        </Form.Item>
        <Form.Item name="owner" label="负责人" rules={[{ required: true, message: '必填' }]}>
          <Input />
        </Form.Item>
        <Form.Item name="mobile" label="手机号" rules={[{ required: true, pattern: /^1[3-9]\\d{9}$/, message: '手机号格式不正确' }]}>
          <Input />
        </Form.Item>
        <Form.Item name="enabled" label="启用状态" valuePropName="checked" initialValue={true}>
          <Switch checkedChildren="启用" unCheckedChildren="禁用" />
        </Form.Item>
      </Form>
    </Modal>
  );
};
export default EditTenantModal;
