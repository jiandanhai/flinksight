import React, { useEffect } from 'react';
import { Form, Input, message, Modal, Select, Switch } from 'antd';
import { getAlertRule, updateAlertRule, createAlertRule } from '@/api/modules';
import type { AlertRuleDTO } from '@/api/dto';

const { Option } = Select;
const LEVELS = [
  { label: '低', value: 1 },
  { label: '中', value: 2 },
  { label: '高', value: 3 },
  { label: '致命', value: 4 },
];

interface Props {
  alertId: number;
  ruleId?: number | null;
  onOk: () => void;
  onCancel: () => void;
}

const EditRuleModal: React.FC<Props> = ({ alertId, ruleId, onOk, onCancel }) => {
  const [form] = Form.useForm();

  useEffect(() => {
    if (ruleId) {
      getAlertRule({ id: ruleId } as any).then((res) => {
        form.setFieldsValue(res.data);
      });
    } else {
      form.resetFields();
    }
  }, [ruleId, form]);

  const handleSubmit = async () => {
    const values = await form.validateFields();

    // 如果后端需要 0/1 而不是 true/false，这里转换一下：
    const patch: any = { ...values };
    if (typeof patch.enable === 'boolean') {
      patch.enable = patch.enable ? 1 : 0;
    }

    if (ruleId) {
      await updateAlertRule(ruleId as any, patch as AlertRuleDTO);
      message.success('编辑成功');
    } else {
      await createAlertRule({ ...patch, alertId } as AlertRuleDTO);
      message.success('新建成功');
    }
    onOk();
  };

  return (
    <Modal
      title={ruleId ? '编辑报警规则' : '新建报警规则'}
      open
      onOk={handleSubmit}
      onCancel={onCancel}
      destroyOnClose
      maskClosable={false}
    >
      <Form form={form} layout="vertical">
        <Form.Item name="name" label="规则名称" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={32} />
        </Form.Item>
        <Form.Item name="metricKey" label="指标 Key" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={64} />
        </Form.Item>
        <Form.Item name="threshold" label="阈值" rules={[{ required: true, message: '必填' }]}>
          <Input type="number" />
        </Form.Item>
        <Form.Item name="compareOp" label="比较符" rules={[{ required: true, message: '必填' }]}>
          <Select>
            <Option value=">">&gt;</Option>
            <Option value="<">&lt;</Option>
            <Option value=">=">&gt;=</Option>
            <Option value="<=">&lt;=</Option>
            <Option value="==">==</Option>
          </Select>
        </Form.Item>
        <Form.Item name="channel" label="通知渠道" rules={[{ required: true, message: '必填' }]}>
          <Select>
            <Option value="email">email</Option>
            <Option value="ding">钉钉</Option>
          </Select>
        </Form.Item>
        <Form.Item name="level" label="报警级别">
          <Select allowClear placeholder="可选">
            {LEVELS.map((l) => (
              <Option value={l.value} key={l.value}>{l.label}</Option>
            ))}
          </Select>
        </Form.Item>
        {/* enable: 表里是 0/1；这里用开关编辑，提交前做了布尔→0/1 的转换 */}
        <Form.Item name="enable" label="是否启用" valuePropName="checked" initialValue>
          <Switch checkedChildren="启用" unCheckedChildren="停用" />
        </Form.Item>
      </Form>
    </Modal>
  );
};

export default EditRuleModal;
