
package org.cloudfoundry.client.lib.archive;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface that represents an application archive (for example a WAR file) that can be uploaded to Cloud Foundry.
 * Archives consist of a {@link #getFilename() filename} and one or more {@link #getEntries() entries}.
 * 
 * @author Phillip Webb
 */
public interface ApplicationArchive {

    /**
     * Returns the filename of the archive (excluding any path).
     * 
     * @return the filename (for example myproject.war)
     */
    String getFilename();

    /**
     * Returns {@link Entry entries} that the archive contains.
     * 
     * @return a collection of entries.
     */
    Iterable<Entry> getEntries();

    /**
     * A single entry contained within an {@link ApplicationArchive}. Entries are used to represent both files and
     * directories.
     */
    public static interface Entry {

        /**
         * Returns <tt>true</tt> if the entry represents a directory.
         * 
         * @return if the entry is a directory.
         */
        boolean isDirectory();

        /**
         * Returns the name of entry including a path. The <tt>'/'</tt> character should be used as a path separator.
         * The name should never start with <tt>'/'</tt>.
         * 
         * @return the name
         */
        String getName();

        /**
         * Returns the size of entry or <tt>0</tt> if the entry is a {@link #isDirectory() directory}.
         * 
         * @return the size
         */
        long getSize();

        /**
         * Returns a SHA1 digest over the {@link #getInputStream() contents} of the entry or <tt>null</tt> if the entry
         * is a {@link #isDirectory() directory}.
         * 
         * @return the SHA1 digest
         */
        byte[] getSha1Digest();

        /**
         * Returns the content of the entry or <tt>null</tt> if the entry is a {@link #isDirectory() directory}. The
         * caller is responsible for closing the stream.
         * 
         * @return the file contents
         * @throws IOException
         */
        InputStream getInputStream() throws IOException;
    }
}
