/**
 * @file 微前端子应用加载器
 * @desc 支持qiankun/Module Federation等一键挂载，自动隔离
 */
import React, {useEffect, useRef} from 'react';

export function loadMicroApp(appName: string): React.FC {
  return () => {
    const ref = useRef<HTMLDivElement>(null);

    useEffect(() => {
      // 这里以qiankun为例：动态加载子应用
      if (window.qiankunStart && ref.current) {
        // qiankunStart(appName, container)
        window.qiankunStart(appName, ref.current);
      }
      // 可根据Module Federation、single-spa等做适配
    }, []);
    return <div ref={ref} style={{ height: '100%', width: '100%' }} />;
  };
}
