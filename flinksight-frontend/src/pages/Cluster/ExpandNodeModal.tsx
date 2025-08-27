/**
 * @file 节点扩容弹窗
 * @desc 支持单节点手动/批量CSV导入扩容，自动接入API/types，扩容成功自动回调，权限/校验/提示完善
 */
import React, { useState } from "react";
import { Button, Form, Input, message, Modal, Switch, Upload, Tooltip } from "antd";
import { UploadOutlined, InfoCircleOutlined } from "@ant-design/icons";

import type { NodeDTO } from '@/api/dto';
import { useUser } from "../../store/user";
import { createNode,batchCreateNode} from "@/api/modules";

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
    try {
      const values = await form.validateFields();
      if (clusterId) values.clusterId = clusterId;
      await createNode(values as NodeDTO);
      message.success("节点扩容成功");
      onOk && onOk({ success: 1, fail: 0 });
      onClose();
      form.resetFields();
    } catch (err) {
      // 校验失败自动提示，无需catch
    }
  };

  // 批量CSV上传
  const handleBatchUpload = async (info: any) => {
    setUploading(true);
    try {
      const file = info.file.originFileObj;
      const reader = new FileReader();
      reader.onload = async (e: any) => {
        const content = e.target.result as string;
        // 假设CSV第一行为字段名，后面每行为节点：name,ip,role[,clusterName]
        const lines = content.split(/\r?\n/).map(l => l.trim()).filter(Boolean);
        if (lines.length < 2) {
          message.error("CSV内容格式错误，至少包含一行数据！");
          setUploading(false);
          return;
        }
        const headers = lines[0].split(",");
        const idx = (name: string) => headers.indexOf(name);
        // 必需字段检测
        if (!['name', 'ip', 'role'].every(h => idx(h) >= 0)) {
          message.error("CSV缺少字段：name,ip,role");
          setUploading(false);
          return;
        }
        // 解析每个节点
        const nodes: NodeDTO[] = lines.slice(1).map(line => {
          const arr = line.split(",");
          const node: NodeDTO = {
            name: arr[idx('name')],
            ip: arr[idx('ip')],
            role: arr[idx('role')],
            clusterName: arr[idx('clusterName')] || "",
            enabled: true,
          };
          if (clusterId) (node as any).clusterId = clusterId;
          return node;
        });
        const filteredNodes = nodes.filter(n => n.name && n.ip && n.role);
        if (filteredNodes.length === 0) {
          message.error("无有效节点数据！");
          setUploading(false);
          return;
        }
        // 批量扩容
        const result = await batchCreateNode(filteredNodes);
        message.success(`批量扩容完成，成功${result.data.success}台，失败${result.data.fail}台`);
        onOk && onOk(result.data);
        setUploading(false);
        onClose();
      };
      reader.readAsText(file);
    } catch (e) {
      message.error("批量导入失败：" + (e as Error).message);
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
          {batchMode ? (
            <>
              支持CSV导入，示例：<code>name,ip,role,clusterName</code>
              <Tooltip title={
                <>
                  <div>CSV首行为表头，后面为节点，如：</div>
                  <pre>name,ip,role,clusterName
worker01,192.168.1.10,worker,集群A
master01,192.168.1.11,master,集群A
</pre>
                </>
              }>
                <InfoCircleOutlined className="ml-1" />
              </Tooltip>
            </>
          ) : "单节点信息手动录入"}
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
        <Form form={form} layout="vertical" autoComplete="off">
          <Form.Item name="name" label="节点名" rules={[{ required: true, message: "请输入节点名" }]}>
            <Input placeholder="worker01" disabled={!canEdit} />
          </Form.Item>
          <Form.Item name="ip" label="节点IP" rules={[
            { required: true, message: "请输入节点IP" },
            { pattern: /^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$/, message: "IP格式不正确" }
          ]}>
            <Input placeholder="192.168.1.100" disabled={!canEdit} />
          </Form.Item>
          <Form.Item name="role" label="角色" rules={[{ required: true, message: "请输入节点角色" }]}>
            <Input placeholder="worker/master" disabled={!canEdit} />
          </Form.Item>
          <Form.Item name="clusterName" label="集群名" required={false}>
            <Input placeholder="集群A" disabled={!canEdit} />
          </Form.Item>
        </Form>
      )}
      {!canEdit && <div className="text-red-500 mt-3">无扩容权限，仅管理员/运维可操作</div>}
    </Modal>
  );
};

export default ExpandNodeModal;
