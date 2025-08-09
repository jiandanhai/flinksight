import React, { useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useUser } from '../../store/user';

const SSO_TOKEN_URL = (import.meta.env.VITE_SSO_TOKEN_URL as string | undefined) || '/api/sso/token';
const SSO_CLIENT_ID = import.meta.env.VITE_SSO_CLIENT_ID as string | undefined;
const SSO_CLIENT_SECRET = import.meta.env.VITE_SSO_CLIENT_SECRET as string | undefined;
const DEFAULT_REDIRECT = '/dashboard';

// 允许的 redirect 前缀（可按需配置多个业务入口）
const REDIRECT_WHITELIST_PREFIXES = [
  '/',               // 只要是站内相对路径都允许
  '/dashboard',
  '/jobs',
  '/clusters',
];

// 防止开放重定向：仅允许相对路径或在白名单前缀内
function getSafeRedirect(input?: string | null): string {
  const raw = (input || '').trim();
  if (!raw) return DEFAULT_REDIRECT;

  try {
    // 绝对地址一律拒绝（可按需放开同源绝对地址）
    const u = new URL(raw, window.location.origin);
    // 仅允许同源且是相对路径（pathname 以 / 开头）
    if (u.origin !== window.location.origin) return DEFAULT_REDIRECT;
    const pathWithQueryHash = u.pathname + u.search + u.hash;
    if (REDIRECT_WHITELIST_PREFIXES.some(p => pathWithQueryHash.startsWith(p))) {
      return pathWithQueryHash || DEFAULT_REDIRECT;
    }
  } catch {
    // 不是合法 URL，当作相对路径判定
    if (raw.startsWith('/') && REDIRECT_WHITELIST_PREFIXES.some(p => raw.startsWith(p))) {
      return raw;
    }
  }
  return DEFAULT_REDIRECT;
}

// 统一提取 search/hash 里的 token/code 参数（兼容部分 IdP 把参数写在 hash）
function extractParams(search: string, hash: string) {
  const sp = new URLSearchParams(search || '');
  const hp = new URLSearchParams((hash || '').replace(/^#/, ''));
  const token = (sp.get('token') || hp.get('token') || '').trim();
  const code = (sp.get('code') || hp.get('code') || '').trim();
  const redirectRaw = sp.get('redirect') || hp.get('redirect') || '';
  const redirect = getSafeRedirect(decodeURIComponent(redirectRaw || ''));
  return { token, code, redirect };
}

// 只在本次会话内防止重复处理（StrictMode 双调用 & 快速刷新）
const HANDLED_FLAG_KEY = 'sso_callback_handled';

const SSOCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { search, hash } = useLocation();
  const { login } = useUser();

  const [status, setStatus] = useState<'working' | 'success' | 'error'>('working');
  const [message, setMessage] = useState<string>('SSO 登录处理中…');
  const handledRef = useRef(false);

  const { token, code, redirect } = useMemo(() => extractParams(search, hash), [search, hash]);

  useEffect(() => {
    // 防抖：避免 StrictMode & 多次进入重复执行
    if (handledRef.current) return;
    if (sessionStorage.getItem(HANDLED_FLAG_KEY) === '1') return;
    handledRef.current = true;
    sessionStorage.setItem(HANDLED_FLAG_KEY, '1');

    const controller = new AbortController();

    (async () => {
      try {
        // 1) 后端已回 token（最推荐）
        if (token) {
          setMessage('已获取 Token，正在登录…');
          await login(token);
          setStatus('success');
          navigate(redirect, { replace: true });
          return;
        }

        // 2) 前端携 code 向后端换 token（前端仅作为代理透传）
        if (code) {
          if (!SSO_CLIENT_ID || !SSO_CLIENT_SECRET) {
            console.warn('[SSO] 缺少 CLIENT_ID/CLIENT_SECRET，仍尝试后端代换…');
          }
          setMessage('已获取授权码，正在换取 Token…');

          const resp = await fetch(SSO_TOKEN_URL, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
              grant_type: 'authorization_code',
              code,
              client_id: SSO_CLIENT_ID,
              client_secret: SSO_CLIENT_SECRET,
              redirect_uri: `${window.location.origin}/login/sso-callback`,
            }),
            signal: controller.signal,
            credentials: 'include', // 若后端需要 Cookie，可保留；不需要可删
          });

          const data = await resp.json().catch(() => ({}));
          const t = data?.access_token || data?.token;
          if (!resp.ok || !t) {
            throw new Error(data?.message || `换取 Token 失败（HTTP ${resp.status}）`);
          }

          setMessage('登录中…');
          await login(t);
          setStatus('success');
          navigate(redirect, { replace: true });
          return;
        }

        throw new Error('缺少 token 或 code 参数');
      } catch (err: any) {
        console.error('[SSO] 回调处理失败：', err);
        setStatus('error');
        setMessage(err?.message || 'SSO 回调处理失败');
        // 2s 后回到登录页，给用户一点阅读时间
        const timer = setTimeout(() => navigate('/login', { replace: true }), 2000);
        return () => clearTimeout(timer);
      }
    })();

    return () => {
      controller.abort();
    };
  }, [token, code, redirect, login, navigate]);

  return (
    <div style={{ maxWidth: 480, margin: '16vh auto', textAlign: 'center', lineHeight: 1.6 }}>
      <h3 style={{ marginBottom: 8 }}>单点登录回调</h3>
      <div style={{ color: status === 'error' ? 'crimson' : '#555' }}>
        {message}
      </div>

      {status === 'error' && (
        <div style={{ marginTop: 16 }}>
          <button
            onClick={() => navigate('/login', { replace: true })}
            style={{ padding: '6px 12px', borderRadius: 8, border: '1px solid #ddd', cursor: 'pointer' }}
          >
            返回登录
          </button>
        </div>
      )}
    </div>
  );
};

export default SSOCallbackPage;
