/**
 * @file 多租户切换/自注册
 * @desc SaaS租户自服务入口，支持切换/加入/创建组织
 */
import React, { useEffect, useState } from 'react';
import { Modal, Button, Select, Form, Input, message } from 'antd';
import http from '@/api/http';

interface Tenant {
  id: number;
  name: string;
}

const TenantSwitcher: React.FC<{ visible: boolean, onClose: () => void }> = ({ visible, onClose }) => {
  const [list, setList] = useState<Tenant[]>([]);
  const [showNew, setShowNew] = useState(false);
  const [form] = Form.useForm();

  useEffect(() => {
    if (visible) http.get('/tenants/self').then(res => setList(res.data || []));
  }, [visible]);

  const handleSwitch = async (tenantId: number) => {
    await http.post(`/tenants/switch`, { tenantId });
    message.success('切换成功');
    onClose();
    window.location.reload();
  };

  const handleCreate = async (values: any) => {
    await http.post('/tenants', values);
    message.success('新租户已创建');
    setShowNew(false);
    onClose();
    window.location.reload();
  };

  return (
    <Modal
      open={visible}
      title="切换/加入租户"
      onCancel={onClose}
      footer={null}
    >
      <div>
        <div>已加入租户：</div>
        <Select
          style={{ width: '100%', marginBottom: 12 }}
          options={list.map(i => ({ label: i.name, value: i.id }))}
          onChange={handleSwitch}
          placeholder="选择要切换的租户"
        />
        <Button type="link" onClick={() => setShowNew(v => !v)}>
          {showNew ? '取消创建' : '我要新建租户'}
        </Button>
        {showNew && (
          <Form form={form} onFinish={handleCreate} layout="vertical">
            <Form.Item label="租户名" name="name" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
            <Form.Item>
              <Button type="primary" htmlType="submit">立即注册</Button>
            </Form.Item>
          </Form>
        )}
      </div>
    </Modal>
  );
};

export default TenantSwitcher;
