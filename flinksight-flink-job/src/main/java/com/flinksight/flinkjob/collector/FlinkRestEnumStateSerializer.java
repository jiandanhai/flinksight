package com.flinksight.flinkjob.collector;

import org.apache.flink.core.io.SimpleVersionedSerializer;

import java.io.*;

public class FlinkRestEnumStateSerializer implements SimpleVersionedSerializer<FlinkRestEnumState> {
    @Override public int getVersion() { return 1; }
    @Override public byte[] serialize(FlinkRestEnumState s) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1);
        DataOutputStream out = new DataOutputStream(baos);
        out.writeBoolean(s != null && s.assigned()); out.flush(); return baos.toByteArray();
    }
    @Override public FlinkRestEnumState deserialize(int version, byte[] b) throws IOException {
        if (version != 1) throw new IOException("unsupported version " + version);
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(b));
        return new FlinkRestEnumState(in.readBoolean());
    }
}
