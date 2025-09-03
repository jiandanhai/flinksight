/*
File: src/pages/audit/AuditTrail.tsx
Purpose: Display recent audit entries for job actions (optional backend).
*/
import React, { useEffect, useState } from "react";
import { RefreshCw } from "lucide-react";
import { api, fmtDate } from "../../api/client";
import { Button } from "../../components/ui/Primitives";

export function AuditTrail(){
    const [logs, setLogs] = useState<any[]>([]);
    const [loading, setLoading] = useState(false);
    const [err, setErr] = useState<string| null>(null);
    const load = async ()=>{
        setLoading(true); setErr(null);
        try { const res = await api<any[]>(`/api/audit/job-actions?limit=20`); setLogs(res || []); }
        catch (e:any) { setErr(e.message || "无法获取审计（后端未接入也属正常）"); }
        finally { setLoading(false); }
    };
    useEffect(()=>{ load(); }, []);
    return (
        <div>
            <div className="mb-3 flex items-center gap-2">
                <Button variant="outline" size="sm" icon={<RefreshCw className="h-4 w-4"/>} onClick={load}>刷新</Button>
                <div className="text-xs text-slate-500">显示最近 20 条</div>
            </div>
            {loading && <div className="text-slate-500 text-sm">加载中…</div>}
            {err && <div className="text-rose-700 bg-rose-50 p-3 rounded-md border mb-2 text-sm">{err}</div>}
            <div className="space-y-2">
                {logs.map((x,i)=> (
                    <div key={i} className="rounded-md border p-3 text-sm">
                        <div className="flex items-center justify-between">
                            <div className="font-medium">{x.action || x.type || "ACTION"}</div>
                            <div className="text-xs text-slate-500">{fmtDate(x.createdAt)}</div>
                        </div>
                        <div className="text-xs text-slate-600 mt-1">{x.operator || x.user || "-"}</div>
                        {x.payload && <pre className="mt-2 bg-slate-50 p-2 rounded border overflow-auto text-xs">{JSON.stringify(x.payload, null, 2)}</pre>}
                    </div>
                ))}
                {(!loading && logs.length===0) && <div className="text-slate-500 text-sm">暂无审计记录（后端接入后显示）</div>}
            </div>
        </div>
    );
}