
package org.cloudfoundry.client.lib.archive;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.springframework.util.FileCopyUtils;

public class ZipApplicationArchive implements ApplicationArchive {

    private ZipFile zipFile;

    private List<Entry> entries;

    private String fileName;
    
    public ZipApplicationArchive(ZipFile zipFile) {
        this.zipFile = zipFile;
        List<Entry> entires = new ArrayList<Entry>();
        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
        while (zipEntries.hasMoreElements()) {
            entires.add(new EntryAdapter(zipEntries.nextElement()));
        }
        this.entries = Collections.unmodifiableList(entires);
        this.fileName = new File(zipFile.getName()).getName();
    }

    public Collection<Entry> getEntries() {
        return entries;
    }
    
    public String getFilename() {
        return fileName;
    }
    
    private class EntryAdapter extends AbstractApplicationArchiveEntry {

        private ZipEntry entry;

        private byte[] bytes;

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
            //FIXME check if zip can read twice
            if (bytes == null) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                FileCopyUtils.copy(zipFile.getInputStream(entry), out);
                this.bytes = out.toByteArray();
            }
            return new ByteArrayInputStream(bytes);
        }

    }

}
