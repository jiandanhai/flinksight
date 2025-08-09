import React, {useEffect, useState} from 'react';
import { api } from 'src/api/gen/client';

import type {JobDTO} from '../../api/gen/data-contracts.ts';
import Loading from '../../components/Loading';

interface Props {
  id: number;
  onBack: () => void;
}

/**
 * 任务详情页
 * - 展示基本信息、状态、配置、实时监控
 */
const JobDetail: React.FC<Props> = ({ id, onBack }) => {
  const [job, setJob] = useState<JobDTO | null>(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    api.getJob(id).then(data => setJob(data)).finally(() => setLoading(false));
  }, [id]);

  if (loading || !job) return <Loading />;

  return (
    <div>
      <button className="mb-6 text-blue-600" onClick={onBack}>← 返回列表</button>
      <h3 className="font-bold text-xl mb-4">{job.name}</h3>
      <div className="grid grid-cols-2 gap-8">
        <div>
          <div className="mb-2"><b>类型：</b>{job.type}</div>
          <div className="mb-2"><b>负责人：</b>{job.owner}</div>
          <div className="mb-2"><b>状态：</b>
            {job.status === 1 ? <span className="text-green-600">运行中</span> :
             job.status === 2 ? <span className="text-gray-500">已停止</span> :
             <span className="text-red-600">异常</span>}
          </div>
          <div className="mb-2"><b>所属集群：</b>{job.cluster?.name || '-'}</div>
          <div className="mb-2"><b>创建时间：</b>{new Date(job.createTime).toLocaleString()}</div>
        </div>
        <div>
          <div className="mb-2"><b>任务配置：</b>
            <pre className="bg-gray-100 p-2 rounded">{job.config}</pre>
          </div>
          <div className="mb-2"><b>备注：</b>{job.remark}</div>
          {/* 可拓展：实时监控、日志、报警等 */}
        </div>
      </div>
    </div>
  );
};
export default JobDetail;
