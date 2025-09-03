/*
File: src/pages/flink/FlinkJobOpsPage.tsx
Purpose: Flink job self-healing operations with double confirmation, async task polling, and last savepoint display.
*/
import React, { useEffect, useRef, useState } from "react";
import { RefreshCw, RotateCcw, Save } from "lucide-react";
import { api } from "../../api/client";
import { Button, Card, Confirm } from "../../components/ui/Primitives";

export function FlinkJobOpsPage(){
    // Form states
    const [clusterName, setClusterName] = useState("");
    const [jobIdHex, setJobIdHex] = useState("");
    const [savepointDir, setSavepointDir] = useState("");
    const [jobName, setJobName] = useState("");
    const [newParallelism, setNewParallelism] = useState<number|"">("");
    const [force, setForce] = useState(false);

    // Savepoint UX
    const [spLoading, setSpLoading] = useState(false);
    const [spPath, setSpPath] = useState<string|null>(null);
    const [spError, setSpError] = useState<string|null>(null);

    // Restart UX
    const [rsLoading, setRsLoading] = useState(false);
    const [rsAck, setRsAck] = useState<{ taskId?: string; message?: string }|null>(null);
    const [taskStatus, setTaskStatus] = useState<string|null>(null);
    const pollRef = useRef<number|undefined>(undefined);

    const triggerSavepoint = async ()=>{
        setSpLoading(true); setSpPath(null); setSpError(null);
        try {
            const body:any = { clusterName, jobIdHex }; if (savepointDir) body.targetDirectory = savepointDir;
            const res = await api<{accepted:boolean; location?:string; message?:string}>("/api/runtime/flink/jobs/savepoint", { method:"POST", body: JSON.stringify(body) });
            if (res.accepted && res.location) setSpPath(res.location); else setSpError(res.message || "未知错误");
        } catch (e:any){ setSpError(e.message); }
        finally { setSpLoading(false); }
    };

    const restartFromLast = async ()=>{
        setRsLoading(true); setTaskStatus(null);
        try {
            const body:any = { clusterName, jobName, force }; if (newParallelism!=="" && newParallelism!=null) body.newParallelism = Number(newParallelism);
            const res = await api<{accepted:boolean; taskId?:string; message?:string}>("/api/runtime/flink/jobs/restart-from-last", { method:"POST", body: JSON.stringify(body) });
            setRsAck(res);
            if (res.accepted && res.taskId){
                // best-effort polling /api/tasks/{id}
                // @ts-ignore
                pollRef.current = window.setInterval(async ()=>{
                    try {
                        const st = await api<{status:string; message?:string}>(`/api/tasks/${res.taskId}`);
                        setTaskStatus(`${st.status}${st.message? ": "+st.message: ""}`);
                        if (["SUCCEEDED","FAILED","CANCELLED","DONE"].includes((st.status||"").toUpperCase())){
                            if (pollRef.current) window.clearInterval(pollRef.current);
                        }
                    } catch { setTaskStatus("任务接口未接入，已降级提示。请在作业部署页查看进度。"); if (pollRef.current) window.clearInterval(pollRef.current); }
                }, 3000);
            }
        } catch (e:any){ setTaskStatus(e.message); }
        finally { setRsLoading(false); }
    };

    useEffect(()=>()=>{ if (pollRef.current) window.clearInterval(pollRef.current); }, []);

    return (
        <div className="mx-auto max-w-4xl p-6 grid gap-4">
            <Card title="触发 Savepoint" subtitle="调用 Flink REST 并轮询 operation，返回保存点路径">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                    <Input label="Cluster Name" value={clusterName} onChange={setClusterName} placeholder="flink-prod-a" />
                    <Input label="Job ID (JID)" value={jobIdHex} onChange={setJobIdHex} placeholder="f3f1a2..." />
                    <Input label="Target Directory（可选）" value={savepointDir} onChange={setSavepointDir} placeholder="file:///... 或 hdfs:///..." />
                </div>
                <div className="mt-4">
                    <Confirm title="确认触发 Savepoint?" message="将向 Flink REST 发送指令，请确认作业状态允许执行此操作。">
                        {(open)=> <Button icon={<Save className="h-4 w-4"/>} loading={spLoading} onClick={open}>触发 Savepoint</Button>}
                    </Confirm>
                    <Button variant="ghost" className="ml-2" icon={<RefreshCw className="h-4 w-4"/>} onClick={()=> { setSpPath(null); setSpError(null); }}>清空结果</Button>
                </div>
                <Result success={!!spPath} text={spPath? `保存点路径：${spPath}` : (spError? `错误：${spError}` : "") }/>
            </Card>

            <Card title="从最近 Savepoint 重启" subtitle="平台创建任务由部署通道执行，返回 taskId 供追踪（可选小步调整并行度）">
                <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                    <Input label="Cluster Name" value={clusterName} onChange={setClusterName} placeholder="flink-prod-a" />
                    <Input label="Job Name（平台注册名）" value={jobName} onChange={setJobName} placeholder="etl_orders_stream" />
                    <Input label="新并行度（可选）" type="number" value={String(newParallelism)} onChange={(v)=> setNewParallelism(v===""? "" : Number(v))} placeholder="不填保持不变" />
                </div>
                <label className="mt-3 inline-flex items-center gap-2 text-sm text-slate-600">
                    <input type="checkbox" checked={force} onChange={(e)=> setForce(e.target.checked)} /> 允许无 savepoint 时直接重提（风险高）
                </label>
                <div className="mt-4">
                    <Confirm title="确认重启作业?" message="将从最近保存点重启（若指定并行度则小步调整），并记录审计。">
                        {(open)=> <Button icon={<RotateCcw className="h-4 w-4"/>} loading={rsLoading} onClick={open}>重启</Button>}
                    </Confirm>
                </div>
                {rsAck && <Result success={true} text={`请求已受理：${rsAck.message || ""}${rsAck.taskId? `（taskId=${rsAck.taskId}）`: ""}`}/>}
                {taskStatus && <Result success={true} text={`任务状态：${taskStatus}`}/>}
            </Card>

            <Card title="操作记录 / 审计" subtitle="后端接入后可在此拉取显示"><AuditTrail/></Card>
        </div>
    );
}

function Input({ label, value, onChange, placeholder, type }: { label: string; value: string; onChange: (v:string)=>void; placeholder?: string; type?: string }){
    return (
        <div>
            <div className="text-xs text-slate-500 mb-1">{label}</div>
            <input className="w-full rounded-md border px-3 py-2" placeholder={placeholder} value={value} onChange={(e)=> onChange(e.target.value)} type={type||"text"}/>
        </div>
    );
}
function Result({ success, text }:{ success:boolean; text:string }){
    if (!text) return null;
    return <div className={cls("mt-3 p-3 rounded-md border text-sm", success? "bg-emerald-50 text-emerald-800" : "bg-rose-50 text-rose-800")}>{text}</div>;
}