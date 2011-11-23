
package org.cloudfoundry.client.lib.archive;

import java.io.IOException;
import java.io.InputStream;

public interface UploadableArchiveEntry {

    public boolean isDirectory();
    
    String getName();

    long getSize() throws IOException;

    byte[] getSha1Digest() throws IOException;

    public InputStream getInputStream() throws IOException;

}
