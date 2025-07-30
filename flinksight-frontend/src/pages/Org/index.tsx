import React, { useEffect, useState } from 'react';
import { getOrgTree, deleteOrgNode } from '../../api/org';
import type { OrgNode } from '../../types/org';
import Loading from '../../components/Loading';

/**
 * 组织架构页面
 * - 展示组织树，支持删除节点
 */
const OrgPage: React.FC = () => {
  const [tree, setTree] = useState<OrgNode[]>([]);
  const [loading, setLoading] = useState(false);

  async function fetchTree() {
    setLoading(true);
    try {
      const data = await getOrgTree();
      setTree(data);
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(node: OrgNode) {
    if (!window.confirm(`确认删除【${node.name}】？`)) return;
    setLoading(true);
    try {
      await deleteOrgNode(node.id);
      fetchTree();
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => { fetchTree(); }, []);

  // 递归渲染树节点
  function renderTree(nodes: OrgNode[]) {
    return (
      <ul className="pl-4">
        {nodes.map(n => (
          <li key={n.id}>
            <span className="mr-2">{n.type === 'department' ? '🏢' : n.type === 'team' ? '👥' : '🏠'} {n.name}</span>
            <button className="text-red-500 ml-2" onClick={() => handleDelete(n)}>删除</button>
            {n.children && n.children.length > 0 && renderTree(n.children)}
          </li>
        ))}
      </ul>
    );
  }

  return (
    <div className="p-6">
      <h2 className="font-bold text-xl mb-6">组织架构</h2>
      {loading ? <Loading /> : renderTree(tree)}
    </div>
  );
};
export default OrgPage;
