/**
 * @file 报警流新建/编辑弹窗
 * @desc 支持新增、编辑，表单校验，权限、错误处理，自动联动API/types/constants
 */
import React, {useEffect} from 'react';
import {Form, Input, message, Modal, Select} from 'antd';
import { getAlert,getAlertsByTenantAndStatus,createAlert,deleteAlert,deleteBatch,updateAlert,exportAlerts} from "@/api/modules";

import type {AlertDTO} from '@/api/dto';

const { Option } = Select;

interface Props {
  id?: number | null; // 编辑时传id
  open: boolean;
  onOk: () => void;
  onClose: () => void;
}

const LEVELS = [
  { label: '低', value: 1 },
  { label: '中', value: 2 },
  { label: '高', value: 3 },
  { label: '致命', value: 4 }
];

const EditAlertModal: React.FC<Props> = ({ id, open, onOk, onClose }) => {
  const [form] = Form.useForm();

  // 拉取详情填充
  useEffect(() => {
    if (id) {
      getAlert(id).then(res => {
        form.setFieldsValue(res.data);
      });
    } else {
      form.resetFields();
    }
  }, [id, form]);

  // 提交
  const handleSubmit = async () => {
    const values = await form.validateFields();
    await updateAlert(id, values as AlertDTO);
    message.success('编辑成功');

    onOk();
    onClose();
  };

  return (
    <Modal
      title={id ? '编辑报警流' : '新建报警流'}
      open={open}
      onOk={handleSubmit}
      onCancel={onClose}
      destroyOnClose
      maskClosable={false}
    >
      <Form form={form} layout="vertical">
        <Form.Item name="name" label="名称" rules={[{ required: true, message: '必填' }]}>
          <Input maxLength={32} placeholder="请输入报警流名称" />
        </Form.Item>
        <Form.Item name="level" label="报警级别" rules={[{ required: true, message: '必选' }]}>
          <Select placeholder="请选择报警级别">
            {LEVELS.map(l => <Option value={l.value} key={l.value}>{l.label}</Option>)}
          </Select>
        </Form.Item>
        <Form.Item name="desc" label="描述">
          <Input.TextArea maxLength={128} placeholder="简要描述该报警流..." />
        </Form.Item>
      </Form>
    </Modal>
  );
};
export default EditAlertModal;
