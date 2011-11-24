
package org.cloudfoundry.client.lib.archive;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public abstract class AbstractApplicationArchiveEntry implements ApplicationArchive.Entry {

    protected static final int UNDEFINED_SIZE = -1;

    private static final int BUFFER_SIZE = 4096;

    private long size = UNDEFINED_SIZE;

    private byte[] sha1Digest;
    
    protected void setSize(long size) {
        this.size = size;
    }
    
    public long getSize() throws IOException {
        if (size == UNDEFINED_SIZE) {
            deduceMissingData();
        }
        return size;
    }
    
    protected void setSha1Digest(byte[] sha1Digest) {
        this.sha1Digest = sha1Digest;
    }
    
    public byte[] getSha1Digest() throws IOException {
        if (sha1Digest == null) {
            deduceMissingData();
        }
        return sha1Digest;
    }

    private void deduceMissingData() throws IOException {
        InputStream inputStream = getInputStream();
        try {
            try {
                MessageDigest digest = (this.sha1Digest == null ? MessageDigest.getInstance("SHA") : null);
                byte[] buffer = new byte[BUFFER_SIZE];
                int byteCount = 0;
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    byteCount += bytesRead;
                    if (digest != null) {
                        digest.update(buffer, 0, bytesRead);
                    }
                }
                if (this.size == UNDEFINED_SIZE) {
                    this.size = byteCount;
                }
                if (this.sha1Digest == null) {
                    this.sha1Digest = digest.digest();
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }

        } finally {
            inputStream.close();
        }
    }

}
