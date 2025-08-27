import React, { useEffect, useState } from "react";
import type { ClusterDTO } from "@/api/dto";
import { createCluster,updateCluster,getCluster } from "@/api/modules";

interface Props {
  id: number | null; // null 为新建，否则为编辑
  open: boolean;
  onClose: () => void;
  onOk: () => void;
}

const EditClusterModal: React.FC<Props> = ({ id, open, onClose, onOk }) => {
  const [form, setForm] = useState<ClusterDTO>({
    name: "", type: "YARN" as any, endpoint: "", version: "", tags: "", remark: "",
  });
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (!open) return;
    if (id) {
      setLoading(true);
      getCluster(id)
        .then((res) => {
          const data = res?.data ?? res;
          setForm({
            name: data.name,
            type: data.type,
            endpoint: data.endpoint,
            version: data.version || "",
            tags: data.tags || "",
            remark: data.remark || "",
          } as ClusterDTO);
        })
        .finally(() => setLoading(false));
    } else {
      setForm({ name: "", type: "YARN" as any, endpoint: "", version: "", tags: "", remark: "" } as ClusterDTO);
    }
  }, [id, open]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    try {
      if (id) {
        await updateCluster(id, form);
      } else {
        await createCluster(form);
      }
      onClose();
      onOk();
    } finally {
      setLoading(false);
    }
  }

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-40">
      <form className="bg-white rounded-xl shadow-xl p-8 w-[420px]" onSubmit={handleSubmit}>
        <h3 className="font-bold text-lg mb-4">{id ? "编辑集群" : "新建集群"}</h3>
        <input className="input mb-4" placeholder="名称" required value={form.name}
               onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))} />
        <select className="input mb-4" value={form.type}
                onChange={(e) => setForm((f) => ({ ...f, type: e.target.value as any }))}>
          <option value="YARN">YARN</option>
          <option value="K8S">K8S</option>
          <option value="Standalone">Standalone</option>
        </select>
        <input className="input mb-4" placeholder="连接地址" value={form.endpoint}
               onChange={(e) => setForm((f) => ({ ...f, endpoint: e.target.value }))} />
        <input className="input mb-4" placeholder="版本" value={form.version}
               onChange={(e) => setForm((f) => ({ ...f, version: e.target.value }))} />
        <input className="input mb-4" placeholder="标签" value={form.tags}
               onChange={(e) => setForm((f) => ({ ...f, tags: e.target.value }))} />
        <input className="input mb-4" placeholder="备注" value={form.remark}
               onChange={(e) => setForm((f) => ({ ...f, remark: e.target.value }))} />

        <div className="flex mt-6 justify-end">
          <button className="btn-secondary mr-2" type="button" onClick={onClose}>取消</button>
          <button className="btn-primary" type="submit" disabled={loading}>提交</button>
        </div>
      </form>
    </div>
  );
};

export default EditClusterModal;
