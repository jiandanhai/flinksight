import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useUser } from "../../context/UserContext"; // ✅ 优化：全局状态管理
import { setApiToken } from "../../api/gen/client";

/** ===== 常量（保留你的命名） ===== */
const SSO_TOKEN_URL = import.meta.env.VITE_SSO_TOKEN_URL || "/api/sso/token";
const SSO_CLIENT_ID = import.meta.env.VITE_SSO_CLIENT_ID;
const SSO_CLIENT_SECRET = import.meta.env.VITE_SSO_CLIENT_SECRET;
const DEFAULT_REDIRECT = "/dashboard";

/** ===== URL 参数处理工具函数 ===== */
function getSafeRedirect(input?: string | null): string {
  const raw = (input || "").trim();
  if (!raw) return DEFAULT_REDIRECT;
  try {
    const u = new URL(raw, window.location.origin);
    if (u.origin !== window.location.origin) return DEFAULT_REDIRECT;
    const path = u.pathname || "/";
    return path === "/" ? DEFAULT_REDIRECT : path;
  } catch {
    return DEFAULT_REDIRECT;
  }
}

function extractParams(search: string, hash: string) {
  const sp = new URLSearchParams(search || "");
  const hp = new URLSearchParams((hash || "").replace(/^#/, ""));

  const token = (sp.get("token") || hp.get("token") || "").trim();
  const code = (sp.get("code") || hp.get("code") || "").trim();
  const redirect = getSafeRedirect(sp.get("redirect") || hp.get("redirect") || "");
  const tenantId = sp.get("tenantId") || hp.get("tenantId") || "";
  return { token, code, redirect, tenantId };
}

function stripCallbackParams() {
  const url = new URL(window.location.href);
  ["token", "code", "redirect", "state", "tenantId"].forEach((k) => url.searchParams.delete(k));
  url.hash = "";
  window.history.replaceState(null, "", url.toString());
}

/** ===== 页面组件主体 ===== */
const SSOCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { search, hash } = useLocation();
  const { login, setUser } = useUser(); // ✅ 优化：使用全局 login 与 setUser
  const [status, setStatus] = useState<"working" | "success" | "error">("working");
  const [message, setMessage] = useState("SSO 登录处理中…");

  const handledRef = useRef(false);
  const { token, code, redirect, tenantId } = extractParams(search, hash);

  useEffect(() => {
    if (handledRef.current || sessionStorage.getItem("ssoCallbackHandled") === "1") return;

    handledRef.current = true;
    sessionStorage.setItem("ssoCallbackHandled", "1");

    const controller = new AbortController();

    const setTokenEverywhere = (t: string) => {
      setApiToken(t);
      sessionStorage.setItem("authToken", t);
    };

    const goto = (path: string) => {
      stripCallbackParams();
      navigate(path || DEFAULT_REDIRECT, { replace: true });
    };

    (async () => {
      try {
        console.log("[SSO Callback] Starting...");
        console.log(`[SSO Callback] Params -> token:${!!token}, code:${!!code}, redirect:${redirect}, tenantId:${tenantId}`);

        if (tenantId) {
          sessionStorage.setItem("tenantId", tenantId);
        }

        const cached = sessionStorage.getItem("authToken");
        if (cached) {
          console.log("[SSO Callback] token from sessionStorage");
          setTokenEverywhere(cached);
          setStatus("success");
          return goto(redirect);
        }

        if (token) {
          console.log("[SSO Callback] token from URL");
          setMessage("已获取 Token，正在登录...");
          setTokenEverywhere(token);
          login(token); // ✅ 调用 login 写入 token
          setStatus("success");

          // ✅ 模拟用户信息，正式环境应从后端 /me 或登录响应中获取
          setUser({ id: 1, tenantId: Number(tenantId) || 1 }); // 可替换为真实返回数据
          return goto(redirect);
        }

        if (code) {
          setMessage("已获取授权码，正在换取 Token...");
          const resp = await fetch(SSO_TOKEN_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              grant_type: "authorization_code",
              code,
              client_id: SSO_CLIENT_ID,
              client_secret: SSO_CLIENT_SECRET,
              redirect_uri: `${window.location.origin}/login/sso-callback`,
            }),
            signal: controller.signal,
            credentials: "include",
          });

          let data: any = {};
          try {
            data = await resp.json();
          } catch {}

          const t = data?.access_token || data?.token;
          if (!resp.ok || !t) {
            throw new Error(data?.message || `换取 Token 失败（HTTP ${resp.status}）`);
          }

          console.log("[SSO Callback] token from backend exchange");
          setMessage("登录中...");
          setTokenEverywhere(t);
          login(t); // ✅ 保存 Token
          setUser({ id: 1, tenantId: Number(tenantId) || 1 }); // ✅ 写入全局用户信息
          setStatus("success");
          return goto(redirect);
        }

        throw new Error("缺少 token 或 code 参数");
      } catch (err: any) {
        console.warn("[SSO Callback] error:", err);
        setStatus("error");
        setMessage(err?.message || "SSO 回调处理失败");
        sessionStorage.removeItem("ssoCallbackHandled");
        setTimeout(() => {
          stripCallbackParams();
          navigate("/login", { replace: true });
        }, 1600);
      }
    })();

    return () => controller.abort();
  }, [token, code, redirect, tenantId, navigate, login, setUser]);

  return (
    <div style={{ maxWidth: 480, margin: "16vh auto", textAlign: "center", lineHeight: 1.6 }}>
      <h3 style={{ marginBottom: 8 }}>单点登录回调</h3>
      <div style={{ color: status === "error" ? "crimson" : "#555" }}>{message}</div>
      {status === "error" && (
        <div style={{ marginTop: 16 }}>
          <button
            onClick={() => navigate("/login", { replace: true })}
            style={{ padding: "6px 12px", borderRadius: 8, border: "1px solid #ddd", cursor: "pointer" }}
          >
            返回登录
          </button>
        </div>
      )}
    </div>
  );
};

export default SSOCallbackPage;
