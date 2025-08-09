/**
 * @file 文件上传与管理
 * @desc 支持多格式上传（CSV/XLSX/JSON/图片等）、列表、批量删除、预览/下载，API/types联动
 */
import React, {useEffect, useState} from 'react';
import {Button, message, Modal, Space, Table, Tag, Upload} from 'antd';
import { api } from 'src/api/gen/client';

import type {FileDTO} from '../../api/gen/data-contracts.ts';

const FileManager: React.FC = () => {
  const [list, setList] = useState<FileDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedRowKeys, setSelectedRowKeys] = useState<number[]>([]);

  // 获取文件列表
  const fetch = async () => {
    setLoading(true);
    try {
      const res = await api.getAllFiles();
      setList(res.data || []);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => { fetch(); }, []);

  // 批量删除
  const handleBatchDelete = () => {
    Modal.confirm({
      title: `确认删除选中${selectedRowKeys.length}个文件？`,
      onOk: async () => {
        await Promise.all(selectedRowKeys.map(id => api.deleteFile(id)));
        message.success('已删除');
        setSelectedRowKeys([]);
        fetch();
      }
    });
  };

  return (
    <div>
      <Upload
        customRequest={async ({ file, onSuccess, onError }) => {
          try {
            await uploadFile(file as File);
            message.success('上传成功');
            fetch();
            onSuccess?.({}, file as File);
          } catch (err) {
            message.error('上传失败');
            onError?.(err as any);
          }
        }}
        multiple
        showUploadList={false}
        style={{ marginBottom: 16 }}
      >
        <Button type="primary">上传文件</Button>
      </Upload>
      <Button danger disabled={!selectedRowKeys.length} style={{ marginLeft: 8 }} onClick={handleBatchDelete}>批量删除</Button>
      <Table
        rowKey="id"
        dataSource={list}
        loading={loading}
        rowSelection={{ selectedRowKeys, onChange: setSelectedRowKeys }}
        columns={[
          { title: '文件名', dataIndex: 'name', render: (v, r) => <a href={r.url} target="_blank" rel="noreferrer">{v}</a> },
          { title: '类型', dataIndex: 'type', render: v => <Tag>{v}</Tag> },
          { title: '大小', dataIndex: 'size', render: v => `${(v/1024).toFixed(2)} KB` },
          { title: '上传时间', dataIndex: 'uploadTime', render: v => new Date(v).toLocaleString() },
          {
            title: '操作',
            render: (_, record) => (
              <Space>
                <a href={record.url} target="_blank" rel="noreferrer">下载</a>
                <Button size="small" danger style={{ marginLeft: 8 }} onClick={() => handleBatchDelete([record.id])}>删除</Button>
              </Space>
            )
          }
        ]}
      />
    </div>
  );
};
export default FileManager;
