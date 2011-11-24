
package org.cloudfoundry.client.lib.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface ApplicationArchive {

    String getFilename();

    Collection<Entry> getEntries();

    public static interface Entry {

        boolean isDirectory();

        String getName();

        long getSize() throws IOException;

        byte[] getSha1Digest() throws IOException;

        InputStream getInputStream() throws IOException;

    }

}
