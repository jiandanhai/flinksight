import React, { useEffect, useMemo, useRef, useState } from 'react';

export interface Col<T> {
  key: string;
  dataIndex?: string;
  title: React.ReactNode;
  width?: number;
  minWidth?: number;
  maxWidth?: number;
  align?: 'left' | 'center' | 'right';
  className?: string;
  render?: (row: T, index: number) => React.ReactNode;
  sorter?: boolean | ((a: T, b: T) => number);
  defaultSortOrder?: 'ascend' | 'descend';
  sortOrder?: 'ascend' | 'descend' | null;
  fixed?: 'left' | 'right';
  resizable?: boolean;
  ellipsis?: boolean;
}

type MaybePageData<T> =
  | T[]
  | { records?: T[]; total?: number; page?: number; size?: number };

export interface RowSelection<T> {
  type?: 'checkbox' | 'radio';
  selectedRowKeys?: React.Key[];
  defaultSelectedRowKeys?: React.Key[];
  getCheckboxProps?: (row: T) => { disabled?: boolean };
  onChange?: (keys: React.Key[], rows: T[]) => void;
}

export interface Expandable<T> {
  expandedRowKeys?: React.Key[];
  defaultExpandedRowKeys?: React.Key[];
  rowExpandable?: (row: T) => boolean;
  expandedRowRender: (row: T, index: number) => React.ReactNode;
  onExpandChange?: (keys: React.Key[], rows: T[]) => void;
  fixedIcon?: boolean;
  iconColWidth?: number;
}

export interface Props<T> {
  columns: Col<T>[];
  data?: MaybePageData<T>;
  loading?: boolean;

  page?: number;       // 1-based
  size?: number;
  total?: number;

  rowKey?: string | ((row: T, index: number) => React.Key);

  onPageChange?: (page: number, size: number) => void;
  onPageSizeChange?: (size: number) => void;
  pageSizeOptions?: number[];

  onSortChange?: (columnKey: string, order: 'ascend' | 'descend' | null) => void;

  rowSelection?: RowSelection<T>;
  expandable?: Expandable<T>;

  emptyText?: React.ReactNode;
  headerRight?: React.ReactNode;
  stickyHeader?: boolean;
  scrollY?: number | string;
  bordered?: boolean;
  striped?: boolean;
  showIndex?: boolean;
  indexTitle?: React.ReactNode;

  onRow?: (row: T, index: number) => React.HTMLAttributes<HTMLTableRowElement>;
  rowClassName?: (row: T, index: number) => string;

  /** 外部额外容器类名（用来加外边距等） */
  className?: string;
}

function PageTable<T>(props: Props<T>) {
  const {
    columns: columnsProp,
    data,
    loading = false,

    page: pageProp,
    size: sizeProp,
    total: totalProp,

    rowKey,
    onPageChange,
    onPageSizeChange,
    pageSizeOptions = [10, 20, 50, 100],
    onSortChange,

    rowSelection,
    expandable,

    emptyText = '无数据',
    headerRight,
    stickyHeader = true,
    scrollY,
    bordered = true,
    striped = false,
    showIndex = false,
    indexTitle = '#',

    onRow,
    rowClassName,
    className = '',
  } = props;

  // -------- 归一化数据 --------
  const normalized = Array.isArray(data) ? { records: data } : (data ?? {});
  const rawRows: T[] = Array.isArray(normalized.records) ? normalized.records : [];

  const page = Number.isFinite(pageProp as number)
    ? Math.max(1, Number(pageProp))
    : Math.max(1, Number(normalized.page ?? 1));

  const size = Number.isFinite(sizeProp as number)
    ? Math.max(1, Number(sizeProp))
    : Math.max(1, Number(normalized.size ?? 20));

  const total = Number.isFinite(totalProp as number)
    ? Math.max(0, Number(totalProp))
    : Math.max(0, Number(normalized.total ?? rawRows.length));

  const totalPage = Math.max(1, Math.ceil((total || 0) / (size || 1)));

  const getRowKey = (row: T, index: number): React.Key => {
    if (typeof rowKey === 'function') return rowKey(row, index);
    if (typeof rowKey === 'string' && (row as any)[rowKey] != null) {
      return String((row as any)[rowKey]);
    }
    return index;
  };

  // -------- 选择（受控/非受控） --------
  const selectionEnabled = !!rowSelection;
  const [innerSelected, setInnerSelected] = useState<React.Key[]>(
    rowSelection?.defaultSelectedRowKeys ?? []
  );
  const selectedKeys = rowSelection?.selectedRowKeys ?? innerSelected;

  const toggleRow = (key: React.Key, r: T) => {
    if (!selectionEnabled) return;
    const disabled = rowSelection?.getCheckboxProps?.(r)?.disabled;
    if (disabled) return;

    if (rowSelection?.type === 'radio') {
      const next = [key];
      rowSelection?.onChange?.(next, rawRows.filter((x, i) => getRowKey(x, i) === key));
      if (!rowSelection?.selectedRowKeys) setInnerSelected(next);
      return;
    }
    const has = selectedKeys.includes(key);
    const next = has ? selectedKeys.filter(k => k !== key) : [...selectedKeys, key];
    rowSelection?.onChange?.(next, rawRows.filter((x, i) => next.includes(getRowKey(x, i))));
    if (!rowSelection?.selectedRowKeys) setInnerSelected(next);
  };

  const toggleAll = () => {
    if (!selectionEnabled || rowSelection?.type === 'radio') return;
    const enabledKeys = rawRows
      .map((r, i) => ({ r, k: getRowKey(r, i) }))
      .filter(({ r }) => !rowSelection?.getCheckboxProps?.(r)?.disabled)
      .map(({ k }) => k);

    const allChecked = enabledKeys.length > 0 && enabledKeys.every(k => selectedKeys.includes(k));
    const next = allChecked ? selectedKeys.filter(k => !enabledKeys.includes(k)) : Array.from(new Set([...selectedKeys, ...enabledKeys]));
    rowSelection?.onChange?.(next, rawRows.filter((x, i) => next.includes(getRowKey(x, i))));
    if (!rowSelection?.selectedRowKeys) setInnerSelected(next);
  };

  // -------- 展开（受控/非受控） --------
  const expandableEnabled = !!expandable;
  const [innerExpanded, setInnerExpanded] = useState<React.Key[]>(
    expandable?.defaultExpandedRowKeys ?? []
  );
  const expandedKeys = expandable?.expandedRowKeys ?? innerExpanded;

  const canExpand = (r: T) => (expandable?.rowExpandable ? expandable.rowExpandable(r) : true);
  const toggleExpand = (key: React.Key, r: T) => {
    if (!expandableEnabled || !canExpand(r)) return;
    const has = expandedKeys.includes(key);
    const next = has ? expandedKeys.filter(k => k !== key) : [...expandedKeys, key];
    expandable?.onExpandChange?.(next, rawRows.filter((x, i) => next.includes(getRowKey(x, i))));
    if (!expandable?.expandedRowKeys) setInnerExpanded(next);
  };

  // -------- 排序（受控/非受控） --------
  const defaultCol = columnsProp.find(c => c.defaultSortOrder);
  const [innerSort, setInnerSort] = useState<{ key: string; order: 'ascend' | 'descend' | null }>(
    defaultCol ? { key: defaultCol.key, order: defaultCol.defaultSortOrder! } : { key: '', order: null }
  );

  const currentSort = useMemo(() => {
    const controlled = columnsProp.find(c => typeof c.sortOrder !== 'undefined');
    if (controlled) return { key: controlled.key, order: (controlled.sortOrder ?? null) as any };
    return innerSort;
  }, [columnsProp, innerSort]);

  const cycleSort = (col: Col<T>) => {
    if (!col.sorter) return;
    const nextOrder =
      currentSort.key !== col.key
        ? 'ascend'
        : currentSort.order === 'ascend'
          ? 'descend'
          : currentSort.order === 'descend'
            ? null
            : 'ascend';
    onSortChange?.(col.key, nextOrder);
    if (typeof col.sortOrder === 'undefined') setInnerSort({ key: col.key, order: nextOrder });
  };

  const sortedRows = useMemo(() => {
    const col = columnsProp.find(c => c.key === currentSort.key);
    if (!col || !col.sorter || !currentSort.order) return rawRows;

    const cmp: ((a: T, b: T) => number) =
      typeof col.sorter === 'function'
        ? col.sorter
        : (a: T, b: T) => {
            const k = col.dataIndex ?? col.key;
            const va = (a as any)?.[k];
            const vb = (b as any)?.[k];
            if (va == null && vb == null) return 0;
            if (va == null) return -1;
            if (vb == null) return 1;
            if (typeof va === 'number' && typeof vb === 'number') return va - vb;
            return String(va).localeCompare(String(vb), 'zh');
          };

    const cloned = [...rawRows].sort(cmp);
    if (currentSort.order === 'descend') cloned.reverse();
    return cloned;
  }, [rawRows, columnsProp, currentSort]);

  // -------- 列宽/固定列 --------
  const DEFAULT_COL_WIDTH = 160;
  const [widthMap, setWidthMap] = useState<Record<string, number>>({});

  useEffect(() => {
    setWidthMap(prev => {
      const next = { ...prev };
      columnsProp.forEach(c => {
        if (typeof next[c.key] !== 'number') {
          const w = typeof c.width === 'number' ? c.width : (c.fixed ? DEFAULT_COL_WIDTH : (c.width as any));
          if (typeof w === 'number') next[c.key] = w;
        }
      });
      return next;
    });
  }, [columnsProp]);

  const startDragRef = useRef<{ key: string; startX: number; startW: number } | null>(null);
  const onResizeStart = (e: React.MouseEvent, key: string) => {
    e.preventDefault();
    const startX = e.clientX;
    const startW = widthMap[key] ?? DEFAULT_COL_WIDTH;
    startDragRef.current = { key, startX, startW };
    const onMove = (ev: MouseEvent) => {
      if (!startDragRef.current) return;
      const { key, startX, startW } = startDragRef.current;
      const dx = ev.clientX - startX;
      setWidthMap(m => {
        const col = columnsProp.find(c => c.key === key)!;
        const min = col.minWidth ?? 60;
        const max = col.maxWidth ?? 1000;
        let w = startW + dx;
        w = Math.max(min, Math.min(max, w));
        return { ...m, [key]: w };
      });
    };
    const onUp = () => {
      startDragRef.current = null;
      window.removeEventListener('mousemove', onMove);
      window.removeEventListener('mouseup', onUp);
    };
    window.addEventListener('mousemove', onMove);
    window.addEventListener('mouseup', onUp);
  };

  const expandIconColWidth = expandableEnabled ? (expandable?.iconColWidth ?? 44) : 0;
  const selectColWidth = selectionEnabled ? (rowSelection?.type === 'radio' ? 44 : 48) : 0;
  const indexColWidth = showIndex ? 60 : 0;

  const renderColumns: Col<T>[] = [
    ...(expandableEnabled ? [{ key: '__expand__', title: '', width: expandIconColWidth, fixed: expandable?.fixedIcon !== false ? 'left' : undefined } as Col<T>] : []),
    ...(selectionEnabled ? [{ key: '__select__', title: '', width: selectColWidth, fixed: 'left' } as Col<T>] : []),
    ...(showIndex ? [{ key: '__index__', title: indexTitle, width: indexColWidth, fixed: 'left' } as Col<T>] : []),
    ...columnsProp,
  ];

  const leftOffsets: Record<string, number> = {};
  const rightOffsets: Record<string, number> = {};
  const leftFixedKeys: string[] = [];
  const rightFixedKeys: string[] = [];

  renderColumns.forEach(c => {
    if (c.fixed === 'left') leftFixedKeys.push(c.key);
    if (c.fixed === 'right') rightFixedKeys.push(c.key);
  });

  let acc = 0;
  leftFixedKeys.forEach(k => {
    const w = k === '__expand__' ? expandIconColWidth
      : k === '__select__' ? selectColWidth
      : k === '__index__' ? indexColWidth
      : (widthMap[k] ?? columnsProp.find(c => c.key === k)?.width ?? DEFAULT_COL_WIDTH);
    leftOffsets[k] = acc;
    acc += Number(w);
  });

  acc = 0;
  [...rightFixedKeys].reverse().forEach(k => {
    const w = widthMap[k] ?? (columnsProp.find(c => c.key === k)?.width as number) ?? DEFAULT_COL_WIDTH;
    rightOffsets[k] = acc;
    acc += Number(w);
  });

  const getCellValue = (row: T, col: Col<T>, index: number) =>
    col.render ? col.render(row, index) : (row as any)?.[col.dataIndex ?? col.key];

  const changePage = (p: number) => onPageChange?.(Math.min(Math.max(1, p), totalPage), size);
  const changeSize = (s: number) => onPageSizeChange?.(Math.max(1, s));

  // —— 单一表头（已合并“全选/展开/序号”到这里）——
  const renderHeaderCell = (col: Col<T>) => {
    const isSorted = currentSort.key === col.key && !!currentSort.order;
    const order = isSorted ? currentSort.order : null;
    const showSorter = !!col.sorter;

    const w = col.key === '__expand__'
      ? expandIconColWidth
      : col.key === '__select__'
        ? selectColWidth
        : col.key === '__index__'
          ? indexColWidth
          : (widthMap[col.key] ?? col.width);

    const style: React.CSSProperties = {
      width: w, minWidth: w, maxWidth: w,
      position: (col.fixed === 'left' || col.fixed === 'right') ? 'sticky' as const : undefined,
      left: col.fixed === 'left' ? leftOffsets[col.key] : undefined,
      right: col.fixed === 'right' ? rightOffsets[col.key] : undefined,
      zIndex: col.fixed ? 2 : 1,
      cursor: showSorter ? 'pointer' : undefined,
    };

    // 特殊列表头内容
    let inner: React.ReactNode = col.title;
    if (col.key === '__expand__') inner = null;
    if (col.key === '__index__') inner = indexTitle;
    if (col.key === '__select__' && rowSelection?.type !== 'radio') {
      inner = (
        <input
          type="checkbox"
          onChange={toggleAll}
          ref={(el) => {
            if (!el) return;
            const enabledKeys = rawRows
              .map((r, i) => ({ r, k: getRowKey(r, i) }))
              .filter(({ r }) => !rowSelection?.getCheckboxProps?.(r)?.disabled)
              .map(({ k }) => k);
            const allChecked = enabledKeys.length > 0 && enabledKeys.every(k => selectedKeys.includes(k));
            const someChecked =
              enabledKeys.some(k => selectedKeys.includes(k)) && !allChecked;
            el.checked = allChecked;
            el.indeterminate = someChecked;
          }}
        />
      );
    }

    return (
      <th
        key={col.key}
        style={style}
        className={`px-3 py-2 ${bordered ? 'border-b' : ''} text-${col.align ?? 'left'} bg-gray-50 ${stickyHeader ? 'sticky top-0 z-10' : ''} ${col.className ?? ''}`}
        onClick={() => (showSorter && !String(col.key).startsWith('__')) && cycleSort(col)}
      >
        <div className="relative flex items-center gap-1">
          <span>{inner}</span>
          {showSorter && !String(col.key).startsWith('__') && (
            <span className="text-xs leading-none select-none">
              <span className={`${order === 'ascend' ? 'text-blue-600' : 'text-gray-300'}`}>▲</span>
              <span className="mx-0.5" />
              <span className={`${order === 'descend' ? 'text-blue-600' : 'text-gray-300'}`}>▼</span>
            </span>
          )}
          {/* 列宽拖拽把手（真实列才显示） */}
          {!String(col.key).startsWith('__') && (col.resizable ?? typeof col.width === 'number') && (
            <span
              onMouseDown={(e) => onResizeStart(e, col.key)}
              onClick={(e) => e.stopPropagation()}
              className="absolute right-0 top-0 h-full w-1 cursor-col-resize select-none"
              style={{ transform: 'translateX(50%)' }}
            />
          )}
        </div>
      </th>
    );
  };

  const containerStyle: React.CSSProperties =
    scrollY ? { maxHeight: typeof scrollY === 'number' ? `${scrollY}px` : scrollY, overflowY: 'auto' } : {};

  return (
    <div className={`bg-white rounded-xl ${bordered ? 'shadow' : ''} overflow-hidden relative ${className}`}>
      {/* 顶部工具区（右对齐） */}
      {headerRight && <div className="px-5 pt-4 pb-2 flex items-center justify-end">{headerRight}</div>}

      {/* 表格 */}
      <div className="overflow-x-auto" style={containerStyle}>
        <table className="min-w-full">
          <thead>
            <tr>
              {renderColumns.map(c => renderHeaderCell(c))}
            </tr>
          </thead>

          <tbody>
            {sortedRows.length > 0 && sortedRows.map((row, idx) => {
              const k = getRowKey(row, idx);
              const checkboxProps = rowSelection?.getCheckboxProps?.(row) ?? {};
              const checked = selectedKeys.includes(k);
              const expanded = expandedKeys.includes(k);
              const expandableThis = expandableEnabled && canExpand(row);

              const rowProps = onRow?.(row, idx) ?? {};
              const extraCls = rowClassName?.(row, idx) ?? '';
              const zebra = striped && idx % 2 === 1 ? 'bg-gray-50' : '';

              return (
                <React.Fragment key={k}>
                  <tr className={`${zebra} hover:bg-gray-100 ${extraCls}`} {...rowProps}>
                    {/* 展开图标列 */}
                    {expandableEnabled && (
                      <td
                        className={`px-3 py-2 ${bordered ? 'border-b' : ''}`}
                        style={{
                          width: expandIconColWidth, minWidth: expandIconColWidth, maxWidth: expandIconColWidth,
                          position: (expandable?.fixedIcon !== false ? 'sticky' : undefined),
                          left: (expandable?.fixedIcon !== false ? leftOffsets['__expand__'] : undefined),
                          zIndex: (expandable?.fixedIcon !== false ? 1 : undefined)
                        }}
                      >
                        <button
                          className={`w-6 h-6 rounded flex items-center justify-center ${expandableThis ? 'hover:bg-gray-200' : 'opacity-40 cursor-not-allowed'}`}
                          onClick={() => expandableThis && toggleExpand(k, row)}
                          disabled={!expandableThis}
                          aria-label="expand"
                        >
                          <span className="inline-block transition-transform" style={{ transform: expanded ? 'rotate(90deg)' : 'rotate(0deg)' }}>▶</span>
                        </button>
                      </td>
                    )}

                    {/* 选择列 */}
                    {selectionEnabled && (
                      <td
                        className={`px-3 py-2 ${bordered ? 'border-b' : ''}`}
                        style={{ width: selectColWidth, minWidth: selectColWidth, maxWidth: selectColWidth, position: 'sticky', left: leftOffsets['__select__'], zIndex: 1 }}
                      >
                        {rowSelection?.type === 'radio' ? (
                          <input type="radio" disabled={checkboxProps.disabled} checked={checked} onChange={() => toggleRow(k, row)} />
                        ) : (
                          <input type="checkbox" disabled={checkboxProps.disabled} checked={checked} onChange={() => toggleRow(k, row)} />
                        )}
                      </td>
                    )}

                    {/* 序号列 */}
                    {showIndex && (
                      <td
                        className={`px-3 py-2 ${bordered ? 'border-b' : ''} text-center`}
                        style={{ width: indexColWidth, minWidth: indexColWidth, maxWidth: indexColWidth, position: 'sticky', left: leftOffsets['__index__'], zIndex: 1 }}
                      >
                        {(page - 1) * size + idx + 1}
                      </td>
                    )}

                    {/* 数据列 */}
                    {columnsProp.map(col => {
                      const w = widthMap[col.key] ?? col.width;
                      const style: React.CSSProperties = {
                        width: w, minWidth: w, maxWidth: w,
                        position: (col.fixed === 'left' || col.fixed === 'right') ? 'sticky' as const : undefined,
                        left: col.fixed === 'left' ? leftOffsets[col.key] : undefined,
                        right: col.fixed === 'right' ? rightOffsets[col.key] : undefined,
                        zIndex: col.fixed ? 0 : undefined,
                        whiteSpace: col.ellipsis ? 'nowrap' : undefined,
                        textOverflow: col.ellipsis ? 'ellipsis' : undefined,
                        overflow: col.ellipsis ? 'hidden' : undefined,
                      };
                      return (
                        <td key={col.key} className={`px-3 py-2 ${bordered ? 'border-b' : ''} text-${col.align ?? 'left'} ${col.className ?? ''}`} style={style}>
                          {getCellValue(row, col, idx)}
                        </td>
                      );
                    })}
                  </tr>

                  {/* 展开区 */}
                  {expandableEnabled && expanded && (
                    <tr>
                      <td className={`p-3 ${bordered ? 'border-b' : ''} bg-gray-50`} colSpan={renderColumns.length}>
                        {expandable!.expandedRowRender(row, idx)}
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              );
            })}

            {(!sortedRows.length && !loading) && (
              <tr>
                <td className="p-6 text-center text-gray-400" colSpan={renderColumns.length}>{emptyText}</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* 加载遮罩 */}
      {loading && (
        <div className="absolute inset-0 bg-white/60 flex items-center justify-center">
          <div className="animate-spin mr-2 h-5 w-5 border-2 border-gray-400 border-t-transparent rounded-full" />
          <span className="text-gray-600">加载中…</span>
        </div>
      )}

      {/* 分页条 */}
      {total > 0 && (
        <div className="px-5 py-4 flex flex-wrap items-center justify-between gap-3 border-t">
          <div className="text-sm text-gray-500">共 {total} 条</div>
          <div className="flex items-center gap-2">
            <button className="px-3 py-1 rounded border" disabled={page <= 1} onClick={() => changePage(1)}>« 首页</button>
            <button className="px-3 py-1 rounded border" disabled={page <= 1} onClick={() => changePage(page - 1)}>上一页</button>
            <span className="text-sm">第 <b>{page}</b> / {Math.max(1, Math.ceil((total || 0) / (size || 1)))} 页</span>
            <button className="px-3 py-1 rounded border" disabled={page >= totalPage} onClick={() => changePage(page + 1)}>下一页</button>
            <button className="px-3 py-1 rounded border" disabled={page >= totalPage} onClick={() => changePage(totalPage)}>末页 »</button>
            <span className="mx-2 text-sm text-gray-500">每页</span>
            <select className="border rounded px-2 py-1" value={size} onChange={(e) => changeSize(Number(e.target.value))}>
              {pageSizeOptions.map(opt => <option key={opt} value={opt}>{opt}</option>)}
            </select>
          </div>
        </div>
      )}
    </div>
  );
}

export default PageTable;
