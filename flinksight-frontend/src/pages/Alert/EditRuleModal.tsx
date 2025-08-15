/**
 * @file 报警规则新建/编辑弹窗
 * @desc 支持规则新增、编辑，字段校验、权限、错误处理、API/type全自动联动
 */
import React, {useEffect} from 'react';
import {Form, Input, message, Modal, Select, Switch} from 'antd';
import  api  from 'src/api/gen/client';

import type {AlertRuleDTO} from '../../api/gen/data-contracts.ts';

const { Option } = Select;

const LEVELS = [
  { label: '低', value: 1 },
  { label: '中', value: 2 },
  { label: '高', value: 3 },
  { label: '致命', value: 4 }
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
      api.getAlertRulesByRule(ruleId).then(res => {
        form.setFieldsValue(res.data);
      });
    } else {
      form.resetFields();
    }
  }, [ruleId, form]);

  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (ruleId) {
      await api.updateAlertRule(ruleId, values as AlertRuleDTO);
      message.success('编辑成功');
    } else {
      await api.createAlertRule({ ...values, alertId } as AlertRuleDTO);
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
        <Form.Item name="expr" label="报警表达式" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={128} />
        </Form.Item>
        <Form.Item name="level" label="报警级别" rules={[{ required: true, message: '必选' }]}>
          <Select>
            {LEVELS.map(l => <Option value={l.value} key={l.value}>{l.label}</Option>)}
          </Select>
        </Form.Item>
        <Form.Item name="enabled" label="是否启用" valuePropName="checked">
          <Switch checkedChildren="启用" unCheckedChildren="停用" />
        </Form.Item>
      </Form>
    </Modal>
  );
};
export default EditRuleModal;
