package de.mastacode.http;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.*;
import org.apache.http.Header;
import org.apache.http.entity.mime.*;

public class CountingRequestEntity implements HttpEntity {
    private final HttpEntity delegate;

    private final ProgressListener listener;

    public CountingRequestEntity(final HttpEntity entity,
            final ProgressListener listener) {
        super();
        this.delegate = entity;
        this.listener = listener;
    }

    public long getContentLength() {
        return this.delegate.getContentLength();
    }

    public boolean isRepeatable() {
        return this.delegate.isRepeatable();
    }

    public void writeTo(final OutputStream out) throws IOException {
        this.delegate.writeTo(new CountingOutputStream(out, this.listener, this.getContentLength()));
    }

    public static class CountingOutputStream extends FilterOutputStream {

        private final ProgressListener listener;

        private long transferred;
        private long total;

        public CountingOutputStream(final OutputStream out,
                final ProgressListener listener, final long totalLength) {
            super(out);
            this.listener = listener;
            this.transferred = 0;
            this.total = totalLength;
        }

        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            this.transferred += len;
            this.listener.onProgress(transferred, total);
        }

        public void write(int b) throws IOException {
            out.write(b);
            this.transferred++;
            this.listener.onProgress(transferred, total);
        }
    }

    @Override
    @Deprecated
    public void consumeContent() throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public InputStream getContent() throws IOException, IllegalStateException {
        return delegate.getContent();
    }

    @Override
    public Header getContentEncoding() {
        return delegate.getContentEncoding();
    }

    @Override
    public boolean isChunked() {
        return delegate.isChunked();
    }

    @Override
    public boolean isStreaming() {
        return delegate.isStreaming();
    }

    @Override
    public Header getContentType() {
        return delegate.getContentType();
    }
}
