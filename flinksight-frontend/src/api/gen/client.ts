import { Api } from "./api";

let currentToken: string | null =
  (typeof window !== "undefined" && (sessionStorage.getItem("authToken") || localStorage.getItem("authToken"))) ||
  null;

const api = new Api({
  baseUrl: import.meta.env.VITE_API_BASE_URL || "",
  secure: true,
  securityWorker: (securityData) => {
    const storageToken =
      typeof window !== "undefined" ? sessionStorage.getItem("authToken") || localStorage.getItem("authToken") : null;
    const token = (securityData ?? currentToken) ?? storageToken ?? null;

    if (token) {
      return { headers: { Authorization: `Bearer ${token}` } };
    }
    return {};
  },
});

export function setApiToken(token?: string) {
  currentToken = token || null;
  api.setSecurityData?.(currentToken);
}

export default api;
