/**
 * @file 前端全局异常捕获与错误上报
 * @desc 自动捕获未处理异常，显示友好提示并上报后端/日志平台
 */
import React from 'react';

type Props = { children: React.ReactNode };
type State = { hasError: boolean; error?: any };

class ErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = { hasError: false };
  }
  static getDerivedStateFromError(error: any) {
    return { hasError: true, error };
  }
  componentDidCatch(error: any, info: any) {
    // 可接入Sentry、阿里云日志服务、飞书/钉钉告警等
    fetch('/api/log/error', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ error: error.toString(), info })
    });
  }
  render() {
    if (this.state.hasError) {
      return <div style={{ padding: 32, color: '#ff4d4f' }}>
        <h2>页面发生错误</h2>
        <pre>{String(this.state.error)}</pre>
        <a href="/">返回首页</a>
      </div>;
    }
    return this.props.children;
  }
}
export default ErrorBoundary;
