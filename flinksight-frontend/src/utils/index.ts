/**
 * 通用工具函数库
 * 严禁与具体业务耦合，仅做全局复用工具
 */

/**
 * 权限校验
 * @param perm 权限字符串
 */
export function checkPermission(perm: string): boolean {
  // 假设从全局状态或本地缓存获取用户权限集
  const userPerms = JSON.parse(localStorage.getItem('user_permissions') || '[]');
  return userPerms.includes(perm);
}


// 时间格式化
export function formatTime(date: Date | string, fmt = 'YYYY-MM-DD HH:mm:ss') {
  const d = typeof date === 'string' ? new Date(date) : date;
  return d.toLocaleString();
}

// 数字缩写（如12345->12.3k）
export function shortenNumber(num: number): string {
  if (num >= 1e8) return (num / 1e8).toFixed(2) + '亿';
  if (num >= 1e4) return (num / 1e4).toFixed(2) + '万';
  return num.toString();
}

// 防抖
export function debounce<T extends (...args: any) => any>(fn: T, wait = 300) {
  let timer: NodeJS.Timeout;
  return (...args: Parameters<T>) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), wait);
  };
}

// 深拷贝
export function deepClone<T>(obj: T): T {
  return JSON.parse(JSON.stringify(obj));
}

// 下载文件
export function downloadFile(url: string, filename: string) {
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
}
