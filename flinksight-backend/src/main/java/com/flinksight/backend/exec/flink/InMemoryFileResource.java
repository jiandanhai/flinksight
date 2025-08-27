package com.flinksight.backend.exec.flink;

import org.springframework.core.io.AbstractResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InMemoryFileResource extends AbstractResource {
    private final String filename;
    private final byte[] bytes;

    public InMemoryFileResource(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }
    @Override public String getDescription() { return "InMemoryFileResource["+filename+"]"; }
    @Override public String getFilename() { return filename; }
    @Override public InputStream getInputStream() { return new ByteArrayInputStream(bytes); }
    @Override public long contentLength() throws IOException { return bytes.length; }
}