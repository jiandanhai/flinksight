package com.flinksight.flinkjob.collector;

import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FlinkRestSplitSerializer implements SimpleVersionedSerializer<FlinkRestSplit> {
    @Override public int getVersion() { return 1; }

    @Override
    public byte[] serialize(FlinkRestSplit s) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(256);
        DataOutputStream out = new DataOutputStream(baos);
        write(out, s.splitId()); write(out, s.restUrl); out.writeLong(s.tenantId);
        write(out, s.env); write(out, s.cluster); write(out, s.clusterId); write(out, s.clusterType);
        out.flush(); return baos.toByteArray();
    }

    @Override
    public FlinkRestSplit deserialize(int version, byte[] b) throws IOException {
        if (version != 1) throw new IOException("unsupported version " + version);
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(b));
        return new FlinkRestSplit(read(in), read(in), in.readLong(), read(in), read(in), read(in), read(in));
    }

    private static void write(DataOutput out, String s) throws IOException {
        if (s == null) { out.writeInt(-1); return; }
        byte[] bs = s.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bs.length); out.write(bs);
    }
    private static String read(DataInput in) throws IOException {
        int len = in.readInt(); if (len < 0) return null;
        byte[] bs = new byte[len]; in.readFully(bs);
        return new String(bs, StandardCharsets.UTF_8);
    }
}
