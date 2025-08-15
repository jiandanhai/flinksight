import React, {useEffect, useState} from 'react';
import  api  from 'src/api/gen/client';

import type {OperationLogDTO} from '../../api/gen/data-contracts.ts';
import PageTable from '../../components/PageTable';
import Loading from '../../components/Loading';

/**
 * 操作日志页面
 * - 查询/删除/展示操作日志
 */
const OperationLogPage: React.FC = () => {
  const [logs, setLogs] = useState<OperationLogDTO[]>([]);
  const [loading, setLoading] = useState(false);

  async function fetchLogs() {
    setLoading(true);
    try {
      const data = await api.getOperationLogs({ page: 1, size: 50 });
      setLogs(data);
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(log: OperationLogDTO) {
    if (!window.confirm(`确定删除该操作日志？`)) return;
    setLoading(true);
    try {
      await api.deleteOperationLog(log.id);
      fetchLogs();
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { fetchLogs(); }, []);

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">操作日志</h2>
      <PageTable<OperationLog>
        columns={[
          { key: 'user', title: '用户' },
          { key: 'action', title: '操作' },
          { key: 'target', title: '目标对象' },
          { key: 'detail', title: '详情' },
          { key: 'ip', title: 'IP' },
          { key: 'time', title: '时间' },
          {
            key: 'op', title: '操作', render: log => (
              <button className="text-red-500" onClick={() => handleDelete(log)}>删除</button>
            )
          }
        ]}
        data={logs}
        loading={loading}
        page={1}
        size={50}
        total={logs.length}
      />
      {loading && <Loading />}
    </div>
  );
};
export default OperationLogPage;
