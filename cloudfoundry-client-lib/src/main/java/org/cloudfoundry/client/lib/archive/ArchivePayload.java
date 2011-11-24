
package org.cloudfoundry.client.lib.archive;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.cloudfoundry.client.lib.CloudFoundryResources;
import org.cloudfoundry.client.lib.io.DynamicZipInputStream;
import org.cloudfoundry.client.lib.io.DynamicZipInputStream.Entry;

public class ArchivePayload {

    private UploadableArchive archive;

    private ArrayList<Entry> entriesToUpload;

    private int totalUncompressedSize;

    public ArchivePayload(UploadableArchive archive, CloudFoundryResources knownRemoteResources) throws IOException {
        this.archive = archive;
        this.totalUncompressedSize = 0;
        Set<String> matches = knownRemoteResources.getAllFilenames();
        this.entriesToUpload = new ArrayList<DynamicZipInputStream.Entry>();
        for (UploadableArchiveEntry entry : archive) {
            if (entry.isDirectory() || !matches.contains(entry.getName())) {
                entriesToUpload.add(new DynamicZipInputStreamEntryAdapter(entry));
                totalUncompressedSize += entry.getSize();
            }
        }
    }

    public UploadableArchive getArchive() {
        return archive;
    }
    
    public InputStream getInputStream() {
        return new DynamicZipInputStream(entriesToUpload);
    }

    private static class DynamicZipInputStreamEntryAdapter implements DynamicZipInputStream.Entry {

        private UploadableArchiveEntry entry;

        public DynamicZipInputStreamEntryAdapter(UploadableArchiveEntry entry) {
            this.entry = entry;
        }

        public String getName() {
            return entry.getName();
        }

        public InputStream getInputStream() throws IOException {
            return entry.getInputStream();
        }

    }

    public int getTotalUncompressedSize() {
        return totalUncompressedSize;
    }

}
