import React, {useEffect, useState} from 'react';
import api from '@/api/api-compat';

import type {OpsTaskDTO} from '@/api/dto';
import PageTable from '../../components/PageTable';
import Loading from '../../components/Loading';

/**
 * 运维自动化任务页面
 * - 运维任务新建、执行、删除
 */
const OpsTaskPage: React.FC = () => {
  const [tasks, setTasks] = useState<OpsTaskDTO[]>([]);
  const [loading, setLoading] = useState(false);

  async function fetchTasks() {
    setLoading(true);
    try {
      const data = await api.getAllOpsTasks({ page: 1, size: 30 });
      setTasks(data);
    } finally {
      setLoading(false);
    }
  }

  async function handleRun(id: number) {
    setLoading(true);
    try {
      await api.runOpsTask(id);
      fetchTasks();
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id: number) {
    setLoading(true);
    try {
      await api.deleteOpsTask(id);
      fetchTasks();
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { fetchTasks(); }, []);

  return (
    <div>
      <h3 className="font-bold text-lg mb-6">运维自动化任务</h3>
      <PageTable<DTO.OpsTaskDTO>
        columns={[
          { key: 'name', title: '任务名' },
          { key: 'script', title: '脚本' },
          { key: 'status', title: '状态' },
          { key: 'runBy', title: '执行人' },
          { key: 'runAt', title: '执行时间' },
          { key: 'result', title: '执行结果' },
          {
            key: 'op', title: '操作', render: t => (
              <>
                <button className="text-blue-600 mr-2" onClick={() => handleRun(t.id)}>执行</button>
                <button className="text-red-500" onClick={() => handleDelete(t.id)}>删除</button>
              </>
            )
          }
        ]}
        data={tasks}
        loading={loading}
        page={1}
        size={30}
        total={tasks.length}
      />
      {loading && <Loading />}
    </div>
  );
};
export default OpsTaskPage;
