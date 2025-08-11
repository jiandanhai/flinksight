import React, { useEffect, useState, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useUser } from "../../store/user";
import { setApiToken } from "../../api/gen/client";  // 正确路径

const SSO_TOKEN_URL = import.meta.env.VITE_SSO_TOKEN_URL || "/api/sso/token";
const SSO_CLIENT_ID = import.meta.env.VITE_SSO_CLIENT_ID;
const SSO_CLIENT_SECRET = import.meta.env.VITE_SSO_CLIENT_SECRET;
const DEFAULT_REDIRECT = "/dashboard";

// Allowed redirect prefixes
const REDIRECT_WHITELIST_PREFIXES = [
  "/",
  "/dashboard",
  "/jobs",
  "/clusters",
];

function getSafeRedirect(input?: string | null): string {
  const raw = (input || "").trim();
  if (!raw) return DEFAULT_REDIRECT;

  try {
    const u = new URL(raw, window.location.origin);
    if (u.origin !== window.location.origin) return DEFAULT_REDIRECT;
    const pathWithQueryHash = u.pathname + u.search + u.hash;
    if (REDIRECT_WHITELIST_PREFIXES.some((p) => pathWithQueryHash.startsWith(p))) {
      return pathWithQueryHash || DEFAULT_REDIRECT;
    }
  } catch {
    if (raw.startsWith("/") && REDIRECT_WHITELIST_PREFIXES.some((p) => raw.startsWith(p))) {
      return raw;
    }
  }
  return DEFAULT_REDIRECT;
}

function extractParams(search: string, hash: string) {
  const sp = new URLSearchParams(search || "");
  const hp = new URLSearchParams((hash || "").replace(/^#/, ""));
  const token = (sp.get("token") || hp.get("token") || "").trim();
  const code = (sp.get("code") || hp.get("code") || "").trim();
  const redirectRaw = sp.get("redirect") || hp.get("redirect") || "";
  const redirect = getSafeRedirect(decodeURIComponent(redirectRaw || ""));
  console.log("[SSO Callback] Extracted Params:", { token, code, redirect }); // 调试日志
  return { token, code, redirect };
}

const SSOCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { search, hash } = useLocation();
  const { login } = useUser();

  const [status, setStatus] = useState<"working" | "success" | "error">("working");
  const [message, setMessage] = useState<string>("SSO 登录处理中…");
  const handledRef = useRef(false);

  const { token, code, redirect } = extractParams(search, hash);

  useEffect(() => {
    if (handledRef.current) return;
    if (sessionStorage.getItem("ssoCallbackHandled") === "1") return;

    handledRef.current = true;
    sessionStorage.setItem("ssoCallbackHandled", "1");

    const controller = new AbortController();

    (async () => {
      try {
        console.log("[SSO Callback] Starting processing...");  // 调试日志
        console.log("[SSO Callback] token:", token);  // 调试日志
        console.log("[SSO Callback] code:", code);  // 调试日志
        console.log("[SSO Callback] redirect:", redirect);  // 调试日志

        const storedToken = sessionStorage.getItem("authToken");

        if (storedToken) {
          console.log("[SSO Callback] Token found in sessionStorage:", storedToken);  // 调试日志
          setApiToken(storedToken);  // 同步 token 到 API 请求头
          setStatus("success");
          console.log("[SSO Callback] Navigating to:", redirect || "/dashboard");
          navigate(redirect || "/dashboard", { replace: true });
          return;
        }

        if (token) {
          console.log("[SSO Callback] Token received from URL:", token);  // 调试日志
          setMessage("已获取 Token，正在登录...");
          await login(token);
          localStorage.setItem("authToken", token); // 存储 token
          sessionStorage.setItem("authToken", token); // 存储 token
          setApiToken(token); // 同步 token 到 API 请求头
          console.log("[SSO Callback] Token stored in localStorage and sessionStorage:", token);  // 调试日志
          setStatus("success");
          console.log("[SSO Callback] Navigating to:", redirect || "/dashboard");
          navigate(redirect || "/dashboard", { replace: true });
          return;
        }

        if (code) {
          console.log("[SSO Callback] Authorization code received:", code);  // 调试日志
          if (!SSO_CLIENT_ID || !SSO_CLIENT_SECRET) {
            console.warn("[SSO] Missing CLIENT_ID/CLIENT_SECRET, trying server-side token exchange...");
          }
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
            credentials: "include", // 确保跨域时携带凭证
          });

          const data = await resp.json();
          const t = data?.access_token || data?.token;

          if (!resp.ok || !t) {
            throw new Error(data?.message || `Failed to exchange token (HTTP ${resp.status})`);
          }

          console.log("[SSO Callback] Token received from backend:", t);  // 调试日志
          setMessage("登录中...");
          await login(t);
          localStorage.setItem("authToken", t);
          sessionStorage.setItem("authToken", t);
          setApiToken(t); // 同步 token 到 API 请求头
          console.log("[SSO Callback] Token stored in localStorage and sessionStorage:", t);  // 调试日志
          setStatus("success");
          console.log("[SSO Callback] Navigating to:", redirect || "/dashboard");
          navigate(redirect || "/dashboard", { replace: true });
          return;
        }

        throw new Error("Missing token or code parameter");
      } catch (err: any) {
        console.error("[SSO Callback] Error occurred during callback processing:", err);  // 调试日志
        setStatus("error");
        setMessage(err?.message || "SSO 回调处理失败");
        const timer = setTimeout(() => navigate("/login", { replace: true }), 2000);
        return () => clearTimeout(timer);
      }
    })();

    return () => {
      controller.abort();
    };
  }, [token, code, redirect, navigate, login]);

  return (
    <div style={{ maxWidth: 480, margin: "16vh auto", textAlign: "center", lineHeight: 1.6 }}>
      <h3 style={{ marginBottom: 8 }}>单点登录回调</h3>
      <div style={{ color: status === "error" ? "crimson" : "#555" }}>
        {message}
      </div>

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
