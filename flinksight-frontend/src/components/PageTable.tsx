import React from 'react';

/**
 * 通用分页表格
 * - 泛型T为数据类型
 * - columns: { key, title, render? }
 */
interface Col<T> {
  key: string;
  title: string;
  render?: (row: T) => React.ReactNode;
}
interface Props<T> {
  columns: Col<T>[];
  data: T[];
  loading?: boolean;
  page: number;
  size: number;
  total: number;
  onPageChange?: (page: number, size: number) => void;
}
function PageTable<T>({ columns, data, loading, page, size, total, onPageChange }: Props<T>) {
  // 简单分页渲染
  const totalPage = Math.ceil(total / size);

  return (
    <div className="bg-white rounded-xl shadow overflow-x-auto">
      <table className="min-w-full">
        <thead>
          <tr>
            {columns.map(col => (
              <th key={col.key} className="p-2 border-b text-left">{col.title}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.map((row, idx) => (
            <tr key={idx} className="hover:bg-gray-50">
              {columns.map(col => (
                <td key={col.key} className="p-2 border-b">
                  {col.render ? col.render(row) : (row as any)[col.key]}
                </td>
              ))}
            </tr>
          ))}
          {(!data.length && !loading) && (
            <tr>
              <td className="p-4 text-center text-gray-400" colSpan={columns.length}>无数据</td>
            </tr>
          )}
        </tbody>
      </table>
      {/* 分页条 */}
      {total > size && (
        <div className="p-4 flex items-center justify-end space-x-2">
          <button className="btn-secondary" disabled={page <= 1} onClick={() => onPageChange?.(page - 1, size)}>上一页</button>
          <span>第 {page} / {totalPage} 页</span>
          <button className="btn-secondary" disabled={page >= totalPage} onClick={() => onPageChange?.(page + 1, size)}>下一页</button>
        </div>
      )}
    </div>
  );
}
export default PageTable;
