
package org.cloudfoundry.client.lib.archive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.util.Assert;

/**
 * Implementation of {@link ApplicationArchive} backed by a {@link ZipFile}.
 * 
 * @author Phillip Webb
 */
public class ZipApplicationArchive implements ApplicationArchive {

    private ZipFile zipFile;

    private List<Entry> entries;

    private String fileName;

    /**
     * Create a new {@link ZipApplicationArchive} instance for the given <tt>zipFile</tt>.
     * @param zipFile The underling zip file
     */
    public ZipApplicationArchive(ZipFile zipFile) {
        Assert.notNull(zipFile, "ZipFile must not be null");
        this.zipFile = zipFile;
        this.entries = adaptZipEntries(zipFile);
        this.fileName = new File(zipFile.getName()).getName();
    }

    private List<Entry> adaptZipEntries(ZipFile zipFile) {
        List<Entry> entires = new ArrayList<Entry>();
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            entires.add(new EntryAdapter(zipEntries.nextElement()));
        }
        return Collections.unmodifiableList(entires);
    }

    public Iterable<Entry> getEntries() {
        return entries;
    }

    public String getFilename() {
        return fileName;
    }

    private class EntryAdapter extends AbstractApplicationArchiveEntry {

        private ZipEntry entry;

        public EntryAdapter(ZipEntry entry) {
            this.entry = entry;
        }

        public boolean isDirectory() {
            return entry.isDirectory();
        }

        public String getName() {
            return entry.getName();
        }

        public long getSize() {
            return entry.getSize();
        }

        public InputStream getInputStream() throws IOException {
            if(isDirectory()) {
                return null;
            }
            return zipFile.getInputStream(entry);
        }
    }
}
