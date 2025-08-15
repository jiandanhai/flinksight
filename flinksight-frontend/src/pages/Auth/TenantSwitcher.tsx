/**
 * @file 多租户切换/自注册
 * @desc SaaS租户自服务入口，支持切换/加入/创建组织
 */
import React, { useEffect, useState } from 'react';
import { Button, Form, Input, message, Modal, Select } from 'antd';
import  api  from 'src/api/gen/client';

// 假设你的 openapi 已生成 Tenant 类型，如果没有可以用下面的 interface 临时代替
interface Tenant {
  id: number;
  name: string;
}

const TenantSwitcher: React.FC<{ visible: boolean, onClose: () => void }> = ({ visible, onClose }) => {
  const [list, setList] = useState<Tenant[]>([]);
  const [showNew, setShowNew] = useState(false);
  const [form] = Form.useForm();

  // 拉取当前用户可切换租户列表
  useEffect(() => {
    if (visible) {
      api.tenantControllerListSelf().then(res => setList(res.data || []));
    }
    // eslint-disable-next-line
  }, [visible]);

  // 切换租户
  const handleSwitch = async (tenantId: number) => {
    await api.tenantControllerSwitch({ tenantId });
    message.success('切换成功');
    onClose();
    window.location.reload();
  };

  // 创建租户
  const handleCreate = async (values: any) => {
    await api.tenantControllerCreate(values);
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
