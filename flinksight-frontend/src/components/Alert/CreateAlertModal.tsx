// src/components/Alert/CreateAlertModal.tsx
import React from 'react';
import {Form, Input, message, Modal, Select} from 'antd';
import { api } from 'src/api/gen/client';

import {AlertLevelDTO} from '../../api/gen/data-contracts.ts';

/**
 * 新建报警流弹窗组件
 * - 真实表单字段、API对接、校验、提交成功自动关闭、支持业务扩展
 */
const CreateAlertModal: React.FC<{
  open: boolean;
  onClose: () => void;
  onSuccess: () => void;
}> = ({ open, onClose, onSuccess }) => {
  const [form] = Form.useForm();
  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      await api.createAlertApi(values);
      message.success('新建报警成功');
      onClose();
      onSuccess();
      form.resetFields();
    } catch (e: any) {
      if (e.errorFields) return; // 校验错误
      message.error('新建报警失败: ' + e.message);
    }
  };
  return (
    <Modal
      title="新建报警"
      open={open}
      onOk={handleOk}
      onCancel={onClose}
      afterClose={() => form.resetFields()}
      destroyOnClose
    >
      <Form form={form} layout="vertical">
        <Form.Item label="报警内容" name="message" rules={[{ required: true, message: '请输入报警内容' }]}> <Input.TextArea rows={3} maxLength={100} /></Form.Item>
        <Form.Item label="级别" name="level" rules={[{ required: true }]}>
          <Select options={AlertLevelDTO.map(l => ({ label: l.label, value: l.value }))} />
        </Form.Item>
        <Form.Item label="租户ID" name="tenantId" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
      </Form>
    </Modal>
  );
};
export default CreateAlertModal;