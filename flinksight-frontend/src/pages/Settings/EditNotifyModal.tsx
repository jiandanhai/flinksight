/**
 * @file 编辑/新建通知渠道弹窗
 * @desc 支持多类型，配置表单自动切换与校验
 */
import React, {useEffect} from 'react';
import {Form, Input, message, Modal, Select, Switch} from 'antd';
import { api } from 'src/api/gen/client';

import type {NotifyChannelDTO} from '../../api/gen/data-contracts.ts';

const { Option } = Select;

interface Props {
  id?: number | null;
  open: boolean;
  onOk: () => void;
  onClose: () => void;
}
const CHANNEL_TYPES = [
  { key: 'email', label: '邮件' },
  { key: 'sms', label: '短信' },
  { key: 'webhook', label: 'Webhook' },
  // 可扩展其它类型
];

const EditNotifyModal: React.FC<Props> = ({ id, open, onOk, onClose }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (id) {
      api.getNotifyChannel(id).then(res => form.setFieldsValue(res.data));
    } else {
      form.resetFields();
    }
  }, [id, form]);

  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (id) {
      await api.updateNotifyChannel(values as NotifyChannelDTO);
      message.success('编辑成功');
    } else {
      await api.createNotifyChannel(values as NotifyChannelDTO);
      message.success('新建成功');
    }
    onOk();
    onClose();
  };

  // 渲染不同类型的表单
  const type = Form.useWatch('type', form);

  return (
    <Modal
      open={open}
      title={id ? '编辑通知渠道' : '新建通知渠道'}
      onOk={handleSubmit}
      onCancel={onClose}
      destroyOnClose
      maskClosable={false}
    >
      <Form form={form} layout="vertical">
        <Form.Item name="name" label="渠道名称" rules={[{ required: true, message: '必填' }]}>
          <Input />
        </Form.Item>
        <Form.Item name="type" label="渠道类型" rules={[{ required: true, message: '必选' }]}>
          <Select>
            {CHANNEL_TYPES.map(t => <Option value={t.key} key={t.key}>{t.label}</Option>)}
          </Select>
        </Form.Item>
        {/* 不同类型动态渲染配置 */}
        {type === 'email' && (
          <>
            <Form.Item name={['config', 'smtp']} label="SMTP服务器" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item name={['config', 'user']} label="发件人账号" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item name={['config', 'password']} label="密码/授权码" rules={[{ required: true }]}>
              <Input.Password />
            </Form.Item>
          </>
        )}
        {type === 'sms' && (
          <>
            <Form.Item name={['config', 'provider']} label="短信服务商" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item name={['config', 'apiKey']} label="API Key" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
          </>
        )}
        {type === 'webhook' && (
          <>
            <Form.Item name={['config', 'url']} label="Webhook URL" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item name={['config', 'secret']} label="签名密钥" rules={[{ required: false }]}>
              <Input />
            </Form.Item>
          </>
        )}
        <Form.Item name="enabled" label="启用状态" valuePropName="checked" initialValue={true}>
          <Switch checkedChildren="启用" unCheckedChildren="禁用" />
        </Form.Item>
      </Form>
    </Modal>
  );
};
export default EditNotifyModal;
