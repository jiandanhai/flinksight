/**
 * @file 节点扩容弹窗
 * @desc 支持单节点手动/批量CSV导入扩容，自动接入API/types，扩容成功自动回调，权限/校验/提示完善
 */
import React, { useState } from "react";
import { Modal, Form, Input, Button, Upload, message, Switch } from "antd";
import { PlusOutlined, UploadOutlined } from "@ant-design/icons";
import { addNode, batchAddNodes } from "../../api/cluster";
import type { NodeCreateReq } from "../../types/cluster";
import { useUser } from "../../store/user";

/**
 * Props:
 * open   弹窗开关
 * onOk   成功回调(返回批量扩容结果)
 * onClose关闭弹窗
 * clusterId 目标集群ID（可选，集群详情扩容场景传递）
 */
interface Props {
  open: boolean;
  onOk?: (result?: { success: number; fail: number; failList?: any[] }) => void;
  onClose: () => void;
  clusterId?: number;
}

const ExpandNodeModal: React.FC<Props> = ({ open, onOk, onClose, clusterId }) => {
  const [form] = Form.useForm();
  const [batchMode, setBatchMode] = useState(false);
  const [uploading, setUploading] = useState(false);
  const { role } = useUser();

  // 只有管理员/运维可扩容
  const canEdit = role === "admin" || role === "ops";

  // 单节点提交
  const handleSubmit = async () => {
    const values = await form.validateFields();
    if (clusterId) values.clusterId = clusterId;
    await addNode(values as NodeCreateReq);
    message.success("节点扩容成功");
    onOk && onOk({ success: 1, fail: 0 });
    onClose();
  };

  // 批量CSV上传
  const handleBatchUpload = async (info: any) => {
    setUploading(true);
    try {
      const file = info.file.originFileObj;
      const reader = new FileReader();
      reader.onload = async (e: any) => {
        const content = e.target.result as string;
        // 假设CSV第一行为字段名，后面每行为节点：name,ip,role
        const lines = content.split(/\r?\n/).map(l => l.trim()).filter(Boolean);
        if (lines.length < 2) {
          message.error("CSV内容格式错误！");
          setUploading(false);
          return;
        }
        // 解析字段
        const headers = lines[0].split(",");
        const idx = (name: string) => headers.indexOf(name);
        // 必需字段检测
        if (!['name', 'ip', 'role'].every(h => idx(h) >= 0)) {
          message.error("CSV缺少字段：name,ip,role");
          setUploading(false);
          return;
        }
        // 解析每个节点
        const nodes: NodeCreateReq[] = lines.slice(1).map(line => {
          const arr = line.split(",");
          const node: NodeCreateReq = {
            name: arr[idx('name')],
            ip: arr[idx('ip')],
            role: arr[idx('role')],
            clusterName: arr[idx('clusterName')] || "", // clusterName可选
            enabled: true,
          };
          if (clusterId) (node as any).clusterId = clusterId;
          return node;
        });
        // 去除空行/错误行
        const filteredNodes = nodes.filter(n => n.name && n.ip && n.role);

        // 批量扩容
        const result = await batchAddNodes(filteredNodes);
        message.success(`批量扩容完成，成功${result.data.success}台，失败${result.data.fail}台`);
        onOk && onOk(result.data);
        setUploading(false);
        onClose();
      };
      reader.readAsText(file);
    } catch (e) {
      message.error("批量导入失败");
      setUploading(false);
    }
  };

  return (
    <Modal
      open={open}
      title="节点扩容"
      onCancel={onClose}
      onOk={batchMode ? undefined : handleSubmit}
      okButtonProps={{ disabled: batchMode || !canEdit }}
      cancelButtonProps={{ disabled: uploading }}
      footer={
        batchMode
          ? null
          : [
              <Button key="cancel" onClick={onClose} disabled={uploading}>取消</Button>,
              <Button key="ok" type="primary" onClick={handleSubmit} disabled={!canEdit || uploading}>
                确认扩容
              </Button>
            ]
      }
      destroyOnClose
      maskClosable={false}
    >
      <div className="mb-4">
        <Switch
          checkedChildren="批量模式"
          unCheckedChildren="单节点"
          checked={batchMode}
          onChange={setBatchMode}
          disabled={uploading}
        />
        <span className="ml-4 text-gray-500">
          {batchMode ? "支持CSV导入，示例：name,ip,role,clusterName" : "单节点信息手动录入"}
        </span>
      </div>
      {batchMode ? (
        <Upload
          accept=".csv"
          beforeUpload={() => false}
          showUploadList={false}
          customRequest={handleBatchUpload}
          disabled={uploading || !canEdit}
        >
          <Button icon={<UploadOutlined />} loading={uploading} disabled={uploading || !canEdit}>
            {uploading ? "上传中..." : "上传CSV批量导入"}
          </Button>
        </Upload>
      ) : (
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="节点名" rules={[{ required: true, message:]()
