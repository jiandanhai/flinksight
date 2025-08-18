// src/pages/logn/SSOCallback.tsx
import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { useUser } from "../../context/UserContext";
// 直接使用你已有的 API 方法（截图里那份）
import { userGetCurrentUser } from "../../api/modules";

const SSO_TOKEN_URL = import.meta.env.VITE_SSO_TOKEN_URL || "/api/sso/token";
const SSO_CLIENT_ID = import.meta.env.VITE_SSO_CLIENT_ID;
const SSO_CLIENT_SECRET = import.meta.env.VITE_SSO_CLIENT_SECRET;
const DEFAULT_REDIRECT = "/dashboard";

// ---- helpers ----
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

function decodeJwtClaims(jwt: string) {
  try {
    const parts = jwt.split(".");
    if (parts.length !== 3) return null;
    const payload = parts[1].replace(/-/g, "+").replace(/_/g, "/");
    const json = JSON.parse(
      decodeURIComponent(
        atob(payload)
          .split("")
          .map((c) => "%" + ("00" + c.charCodeAt(0).toString(16)).slice(-2))
          .join(""),
      ),
    );
    return json;
  } catch {
    return null;
  }
}

// ---- page ----
const SSOCallbackPage: React.FC = () => {
  const navigate = useNavigate();
  const { search, hash } = useLocation();
  const { login, setUser } = useUser();

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
      sessionStorage.setItem("authToken", t);
    };

    const goto = (path: string) => {
      stripCallbackParams();
      navigate(path || DEFAULT_REDIRECT, { replace: true });
    };

    const fetchCurrentUser = async () => {
      console.info("[SSO/me:snapshot]", {
        tokenLen: (sessionStorage.getItem("authToken") || "").length,
      });
      // ✅ 这里调用你统一封装的接口方法，而不是硬编码 URL
      const user = await userGetCurrentUser();
      if (!user?.id) throw new Error("用户信息不完整");
      return user;
    };

    (async () => {
      try {
        console.info("[SSO] Starting...");
        console.info("[SSO] Params", { token: !!token, code: !!code, redirect, tenantId });

        if (tenantId) sessionStorage.setItem("tenantId", String(tenantId));

        if (token) {
          const claims = decodeJwtClaims(token);
          if (claims) {
            console.info("[SSO] token claims", {
              iss: claims.iss,
              aud: claims.aud,
              sub: claims.sub,
              exp: claims.exp,
              iat: claims.iat,
            });
            if (claims?.exp) {
              console.info("[SSO] token exp(human)", new Date(claims.exp * 1000).toISOString());
            }
          }
        }

        const cached = sessionStorage.getItem("authToken");
        if (cached) {
          console.info("[SSO] use cached token");
          setTokenEverywhere(cached);
          login(cached);
          await Promise.resolve();
          const user = await fetchCurrentUser();
          setUser(user);
          setStatus("success");
          return goto(redirect);
        }

        if (token) {
          console.info("[SSO] use token from URL");
          setMessage("已获取 Token，正在登录...");
          setTokenEverywhere(token);
          login(token);
          await Promise.resolve();
          const user = await fetchCurrentUser();
          setUser(user);
          setStatus("success");
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

          const data = await resp.json();
          const t = data?.access_token || data?.token;
          if (!resp.ok || !t) throw new Error(data?.message || `换取 Token 失败（HTTP ${resp.status}）`);

          console.info("[SSO] token from backend exchange");
          setMessage("登录中...");
          setTokenEverywhere(t);
          login(t);
          await Promise.resolve();
          const user = await fetchCurrentUser();
          setUser(user);
          setStatus("success");
          return goto(redirect);
        }

        throw new Error("缺少 token 或 code 参数");
      } catch (err: any) {
        const r = err?.response;
        console.warn("[SSO] error", {
          name: err?.name,
          message: err?.message,
          status: r?.status,
          url: r?.config?.url,
        });
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
