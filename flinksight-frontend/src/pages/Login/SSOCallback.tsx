import React, { useEffect, useState, useRef } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useUser } from "../../store/user"; // Assuming useUser is your context

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

// Prevent open redirects
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

// Extract token and redirect params from URL
function extractParams(search: string, hash: string) {
  const sp = new URLSearchParams(search || "");
  const hp = new URLSearchParams((hash || "").replace(/^#/, ""));
  const token = (sp.get("token") || hp.get("token") || "").trim();
  const code = (sp.get("code") || hp.get("code") || "").trim();
  const redirectRaw = sp.get("redirect") || hp.get("redirect") || "";
  const redirect = getSafeRedirect(decodeURIComponent(redirectRaw || ""));
  return { token, code, redirect };
}

const SSOCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { search, hash } = useLocation();
  const { login } = useUser(); // Assuming you have a context or state management system

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
        const storedToken = sessionStorage.getItem("authToken");
        console.log('Checking sessionStorage for token:', storedToken);

        if (storedToken) {
          setStatus("success");
          console.log("Token found in sessionStorage, skipping login and navigating to:", redirect);
          navigate(redirect || "/dashboard", { replace: true });
          return;
        }

        if (token) {
          setMessage("已获取 Token，正在登录...");
          await login(token); // Store token in sessionStorage and update user state
          console.log('Token stored in sessionStorage:', sessionStorage.getItem('authToken'));
          localStorage.setItem('authToken', token); // Also store it in localStorage
          console.log('Token stored in localStorage:', localStorage.getItem('authToken'));
          setStatus("success");
          console.log("Token received, navigating to:", redirect);
          navigate(redirect || "/dashboard", { replace: true });
          return;
        }

        if (code) {
          if (!SSO_CLIENT_ID || !SSO_CLIENT_SECRET) {
            console.warn("[SSO] 缺少 CLIENT_ID/CLIENT_SECRET，仍尝试后端代换…");
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
            credentials: "include",
          });

          const data = await resp.json();
          const t = data?.access_token || data?.token;

          if (!resp.ok || !t) {
            throw new Error(data?.message || `换取 Token 失败（HTTP ${resp.status}）`);
          }

          setMessage("登录中...");
          await login(t);
          console.log('Token stored in sessionStorage:', sessionStorage.getItem('authToken'));
          localStorage.setItem('authToken', t);
          console.log('Token stored in localStorage:', localStorage.getItem('authToken'));
          setStatus("success");
          console.log("Navigating to:", redirect);
          navigate(redirect || "/dashboard", { replace: true });
          return;
        }

        throw new Error("缺少 token 或 code 参数");
      } catch (err: any) {
        console.error("[SSO] 回调处理失败：", err);
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
